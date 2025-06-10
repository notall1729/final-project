package aut.ap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.*;

import java.lang.module.Configuration;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        try {
            sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();

            System.out.println("Welcome to our system!");
            System.out.println("[L]ogin, [S]ign up");
            String command = scanner.nextLine();

            if (command.equals("L")) {
                login();
            }
            else if (command.equals("S")) {
                signUp();
            } else {
                System.out.println("Invalid choice. please try again.");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public static void login(){
        System.out.println("----------------Log in-----------------");

        System.out.println("Email: ");
        String email = scanner.nextLine();

        System.out.println("Password: ");
        String password = scanner.nextLine();

        try (Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where email = :email and password = : password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            User user = query.uniqueResult();

            if (user != null){
                System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
            }
            else {
                System.out.println("Invalid email or password");
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void signUp(){
        User user;
        System.out.println("---------------Sign up-----------------");
        System.out.print("First name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Age: ");
        Integer age = scanner.nextInt();

        System.out.print("Email: ");
        scanner.nextLine();
        String email = scanner.nextLine();

        System.out.print("password: ");
        String password = scanner.nextLine();
        if (password.length() < 8){
            System.out.println("Weak password.");
            signUp();
        }

        try(Session session = sessionFactory.openSession()){
            Query<User> query = session.createQuery("from User where email = : email", User.class);
            query.setParameter("email", email);
            User existingUser = query.uniqueResult();

            if (existingUser != null){
                System.out.println("An account with this email already exists.");
                return;
            }

            user = new User(firstName, lastName, age, email, password);

            Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();

            System.out.println("successful Registration!");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }


    }


}