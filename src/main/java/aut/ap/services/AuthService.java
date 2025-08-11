package aut.ap.services;

import aut.ap.DatabaseManager;
import aut.ap.User;
import org.hibernate.Session;

//manage login and sign up
public class AuthService {
    public static String signUp(String name, String email, String password){
        try(Session session = DatabaseManager.getSession()){
            session.beginTransaction();

            User existingUser = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email.endsWith("@milou.com") ? email : email + "@milou.com")
                    .uniqueResult();

                if (existingUser != null) {
                    return "Error: This email is already registered!";
                }
                if (password.length() < 8) {
                    return "Error: Password must be at least 8 characters long.";
                }

            User newUser = new User(name, email, password);
            session.persist(newUser);
            session.getTransaction().commit();

            System.out.println("Your new account is created.\n Go ahead and login!");
            return "true";
        }
    }

    public static User login(String email, String password){
        try(Session session = DatabaseManager.getSession()){
            User user = session.createQuery("from User where email = :email and password = :password", User.class)
                    .setParameter("email", email.endsWith("@milou.com") ? email : email + "@milou.com")
                    .setParameter("password", password)
                    .uniqueResult();

            if (user == null){
                System.out.println("Error: Invalid email or password!");
                return null;
            }

            return user;
        }
    }
}
