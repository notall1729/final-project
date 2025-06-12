package aut.ap.services;

import aut.ap.DatabaseManager;
import aut.ap.Email;
import aut.ap.EmailRecipient;
import aut.ap.User;
import org.hibernate.Session;

import java.util.List;

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
        }
    }
}
