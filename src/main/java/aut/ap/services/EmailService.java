package aut.ap.services;

import aut.ap.DatabaseManager;
import aut.ap.Email;
import aut.ap.EmailRecipient;
import aut.ap.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmailService {
    public static void sendEmail(User sender, List<String> recipientEmails, String subject, String body){
        try(Session session = DatabaseManager.getSession()){
            session.beginTransaction();

            Email email = new Email(sender, subject, body);
            session.persist(email);

            for (String recipientEmail : recipientEmails){
                User recipient = session.createQuery("from User where email = :email", User.class)
                        .setParameter("email", recipientEmail.endsWith("@milou.com") ? recipientEmail : recipientEmail + "@milou.com")
                        .uniqueResult();

                if (recipient != null){
                    EmailRecipient emailRecipient = new EmailRecipient(email, recipient);
                    session.persist(emailRecipient);
                } else {
                    System.out.println("Error: Recipient '" + recipientEmail + "' not found!");
                }
            }

            session.getTransaction().commit();
            System.out.println("Successfully sent your email.\nCode: " + email.getId());
        } catch (Exception e){
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void viewEmails(User user, String type){
        Scanner scanner = new Scanner(System.in);
        try(Session session = DatabaseManager.getSession()){
            List<Email> emails = new ArrayList<>();

            switch (type.toLowerCase()){
                case "a":
                    emails = session.createQuery("select e from Email e join EmailRecipient r on e.id = r.email.id where r.recipient.id = :userId order by e.sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    System.out.println("All Emails: ");
                    break;

                case "u":
                    emails = session.createQuery("select e from Email e join EmailRecipient r on e.id = r.email.id where r.recipient.id = :userId and r.isRead = false order by e.sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    System.out.println("Unread Emails: ");
                    break;

                case "s":
                    emails = session.createQuery("select from Email where sender.id = :userId order by sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    System.out.println("Sent Emails: ");
                    break;

                case "c":
                   System.out.println("Code:");
                   String code = scanner.nextLine();
                   emails = readByCode(code);
                   break;

                default:
                    System.out.println("Invalid option!");
                    return;
            }

            if (emails.isEmpty()){
                System.out.println("No emails found.");
                return;
            }

            for (Email email : emails){
                String recipients = session.createQuery("select u.email from User u join EmailRecipient r on u.id = r.recipient.id where r.email.id = :emailId", String.class)
                        .setParameter("emailId", email.getId())
                        .getResultList()
                        .stream()
                        .collect(Collectors.joining(", "));

                System.out.print("+ " + email.getSender().getEmail() + " - " + email.getSubject() + " (" + email.getId() + ")");

           if (!recipients.isEmpty()){
               System.out.println(" To: " + recipients);
              }
           System.out.println(" Date: " + email.getSentAt());
            }
        }catch (Exception e){
            System.out.println("Error viewing emails: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Email> readByCode(String code) {
        try (Session session = DatabaseManager.getSession()) {
            List<Email> emails = new ArrayList<>();
            emails = session.createQuery("select from Email where id = :code", Email.class)
                    .setParameter("code", code)
                    .getResultList();

            return emails;
        } catch (Exception e) {
            System.out.println("Error viewing emails: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void readEmail(User user, String emailCode){
        try(Session session = DatabaseManager.getSession()){
            session.beginTransaction();

            Email email = session.createQuery("from Email where id = :emailCode", Email.class)
                    .setParameter("emailCode", emailCode)
                    .uniqueResult();

            if (email == null){
                System.out.println("Error: Email not found!");
                return;
            }

            boolean isRecipient = session.createQuery("select COUNT(r) from EmailRecipient r where r.email.id = :emailId and r.recipient.id = :userId", Long.class)
                    .setParameter("emailId", email.getId())
                    .setParameter("userId", user.getId())
                    .getSingleResult() > 0;

            if (!isRecipient && email.getSender().getId() != user.getId()){
                System.out.println("You cannot read this email.");
                return;
            }

            String recipients = session.createQuery("select u.email from User u join EmailRecipient r on u.id = r.recipient.id where r.email.id = :emailId", String.class)
                    .setParameter("emailId", email.getId())
                    .getResultList()
                    .stream()
                    .collect(Collectors.joining(", "));

            System.out.println("\nCode: " + email.getId());
            System.out.println("Recipient(s): " + recipients);
            System.out.println("Subject: " + email.getSubject());
            System.out.println("Date: " + email.getSentAt());
            System.out.println("\n" + email.getBody());

            session.createQuery("update EmailRecipient set isRead = true where email.id = :emailId and recipient.id = :userId")
                    .setParameter("emailId", email.getId())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
