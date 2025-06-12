package aut.ap;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.*;
import org.hibernate.cfg.Configuration;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true){
            System.out.println("[L]ogin, [S]ign up: ");
            String input = scanner.nextLine().toLowerCase();

            if (input.equals("l") || input.equals("login")){
                System.out.println("Email: ");
                String email = scanner.nextLine();

                System.out.println("Password: ");
                String password = scanner.nextLine();

                AuthService.login(email, password);
            }
            else if (input.equals("s") || input.equals("sign up")){
                System.out.println("Name: ");
                String name = scanner.nextLine();

                System.out.println("Email: ");
                String email = scanner.nextLine();

                System.out.println("Password: ");
                String password = scanner.nextLine();

                AuthService.signUp(name, email, password);
            }
            else {
                System.out.println("Invalid option. Please enter [L]ogin or [S]ign up.");
            }
        }
    }
}