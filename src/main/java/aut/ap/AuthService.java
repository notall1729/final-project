package aut.ap;

import org.hibernate.Session;

//manage login and sign up
public class AuthService {
    public static boolean signUp(String name, String email, String password){
        try(Session session = DatabaseManager.getSession()){
            session.beginTransaction();

            User existingUser = session.createQuery("from User where email = :email", User.class)
                    .setParameter("email", email.endsWith("@milou.com") ? email : email + "@milou.com")
                    .uniqueResult();

            if (existingUser != null || existingUser.getPassword().length() > 8){
                if (existingUser != null) {
                    System.out.println("Error: This email is already registered!");
                }
                if (existingUser.getPassword().length() < 8){
                    System.out.println("Error: Password must be at least 8 characters long.");
                }
                return false;
            }

            User newUser = new User(name, email, password);
            session.persist(newUser);
            session.getTransaction().commit();

            System.out.println("Your new account is created. Go ahead and login!");
            return true;
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

            System.out.println("Welcome back, " + user.getName() + "!");
            return user;
        }
    }
}
