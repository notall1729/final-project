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
    public static String sendEmail(User sender, List<String> recipientEmails, String subject, String body){
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
                    return "Error: Recipient '" + recipientEmail + "' not found!";
                }
            }

            session.getTransaction().commit();
            return "<html>Successfully sent your email.<br>Code: <html>" + email.getId();
        } catch (Exception e){
            System.out.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public static String  viewEmails(User user, String type){
        Scanner scanner = new Scanner(System.in);
        try(Session session = DatabaseManager.getSession()){
            List<Email> emails = new ArrayList<>();
            String result = "";

            switch (type.toLowerCase()){
                case "a":
                    emails = session.createQuery("select e from Email e join EmailRecipient r on e.id = r.email.id where r.recipient.id = :userId order by e.sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    result = result + "<html>All Emails: <br><html>";
                    break;

                case "u":
                    emails = session.createQuery("select e from Email e join EmailRecipient r on e.id = r.email.id where r.recipient.id = :userId and r.isRead = false order by e.sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    result = result + "<html>Unread Emails: <br><html>";
                    break;

                case "s":
                    emails = session.createQuery("select e from Email e where e.sender.id = :userId order by e.sentAt desc", Email.class)
                            .setParameter("userId", user.getId())
                            .getResultList();

                    result = result + "<html>Sent Emails: <br><html>";
                    break;

                case "c":
                   System.out.println("Code:");
                   String code = scanner.nextLine();
                   emails = readByCode(code);
                   break;
            }

            if (emails.isEmpty()){
                return "No emails found.";

            }

            for (Email email : emails){
                String recipients = session.createQuery("select u.email from User u join EmailRecipient r on u.id = r.recipient.id where r.email.id = :emailId", String.class)
                        .setParameter("emailId", email.getId())
                        .getResultList()
                        .stream()
                        .collect(Collectors.joining(", "));

                result = result + "+ " + email.getSender().getEmail() + " - " + email.getSubject() + " (" + email.getId() + "<html>)<br><html>";
            }
            return result;
        }catch (Exception e){
            System.out.println("Error viewing emails: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public static List<Email> readByCode(String code) {
        try (Session session = DatabaseManager.getSession()) {
            List<Email> emails = new ArrayList<>();
            emails = session.createQuery("from Email where id = :code", Email.class)
                    .setParameter("code", code)
                    .getResultList();

            return emails;
        } catch (Exception e) {
            System.out.println("Error viewing emails: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static String readEmail(User user, String emailCode){
        try(Session session = DatabaseManager.getSession()){
            session.beginTransaction();

            Email email = session.createQuery("from Email where id = :emailCode", Email.class)
                    .setParameter("emailCode", emailCode)
                    .uniqueResult();

            if (email == null){
                return "Error: Email not found!";

            }

            boolean isRecipient = session.createQuery("select COUNT(r) from EmailRecipient r where r.email.id = :emailId and r.recipient.id = :userId", Long.class)
                    .setParameter("emailId", email.getId())
                    .setParameter("userId", user.getId())
                    .getSingleResult() > 0;

            if (!isRecipient && email.getSender().getId() != user.getId()){
              return "You cannot read this email.";

            }

            String recipients = session.createQuery("select u.email from User u join EmailRecipient r on u.id = r.recipient.id where r.email.id = :emailId", String.class)
                    .setParameter("emailId", email.getId())
                    .getResultList()
                    .stream()
                    .collect(Collectors.joining(", "));

            session.createQuery("update EmailRecipient set isRead = true where email.id = :emailId and recipient.id = :userId")
                    .setParameter("emailId", email.getId())
                    .setParameter("userId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();

            return "<html>" + "Code: " + email.getId() + "<br><br>" + " Recipient(s): " + recipients +
                    "<br><br>" + " Subject: " + email.getSubject() + "<br><br>" + "Date: " + email.getSentAt() + "<br><br>" + "Body: " + email.getBody() + "<html>";

        }
    }

    public static String  replyToEmail(User replier, String originalCode, String body){
        try(Session session = DatabaseManager.getSession()) {
            session.beginTransaction();

            Email originalEmail = session.createQuery("from Email where id = :code", Email.class)
                    .setParameter("code", originalCode)
                    .uniqueResult();
            if(originalEmail == null){
                session.getTransaction().commit();
                return "Error: Email not found!";
            }
            Long count = session.createQuery("select count(r) from EmailRecipient r where r.email.id = :emailId and r.recipient.id = :userId", Long.class)
                    .setParameter("emailId", originalEmail.getId())
                    .setParameter("userId", replier.getId())
                    .uniqueResult();

            boolean isRecipient = count != null && count > 0;
            boolean isSender = originalEmail.getSender().getId() == replier.getId();

            if (!isRecipient && !isSender){
                session.getTransaction().commit();
                return "Error: You can only reply to emails that are sent to you or sent by you!";
            }

                Email replyEmail = new Email(replier, "[Re] " + originalEmail.getSubject(), body);
                session.persist(replyEmail);

               User replyRecipient = originalEmail.getSender();
               EmailRecipient replyEmailRecipient = new EmailRecipient(replyEmail, replyRecipient);
               session.persist(replyEmailRecipient);

                session.getTransaction().commit();
                return "<html>" + "Successfully sent your reply to email " + originalCode + "<br>" + "Code: " + replyEmail.getId() + "<html>";
            }catch (Exception e){
        System.out.println("Error replying email: " + e.getMessage());
        e.printStackTrace();
    }
        return "";
    }
    public static String  forwardEmail(User sender, String originalCode, List<String> newRecipients){
        try(Session session = DatabaseManager.getSession()) {
            session.beginTransaction();

            Email originalEmail = session.createQuery("from Email where id = :code", Email.class)
                    .setParameter("code", originalCode)
                    .uniqueResult();

            if(originalEmail == null){
                session.getTransaction().commit();
                return "Email not found!";
            }
            boolean isRecipient = session.createQuery("select count(r) from EmailRecipient r where r.email.id = :emailId and r.recipient.id = :userId", Long.class)
                    .setParameter("emailId", originalEmail.getId())
                    .setParameter("userId", sender.getId())
                    .uniqueResult() > 0;

            boolean isSender = originalEmail.getSender().getId() == sender.getId();

            if (!isRecipient && !isSender){
                session.getTransaction().commit();
                return "You can only forward emails that were sent to you or sent by you!";
            }

            Email forwardedEmail = new Email(sender, "[FW] " + originalEmail.getSubject(), originalEmail.getBody());
            session.persist(forwardedEmail);

            for (String recipientEmail : newRecipients){
                User recipient = session.createQuery("from User where email = :email", User.class)
                        .setParameter("email", recipientEmail.endsWith("milou.com") ? recipientEmail : recipientEmail + "milou.com")
                        .uniqueResult();

                if (recipient != null){
                    EmailRecipient emailRecipient = new EmailRecipient(forwardedEmail, recipient);
                    session.persist(emailRecipient);
                } else {
                     return "Error: Recipient '" + recipientEmail + "'not found!";
                }
            }

            session.getTransaction().commit();
            return "<html>" + "Successfully forwarded your email." + "<br>" + "Code: " + forwardedEmail.getId() + "<html";
        }catch (Exception e){
            System.out.println("Error forwarding email: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }
}
