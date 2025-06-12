package aut.ap;

import aut.ap.services.AuthService;
import aut.ap.services.EmailService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true){
            System.out.println("[L]ogin, [S]ign up: ");
            String input = scanner.nextLine().toLowerCase();

            User user = null;

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

                if(AuthService.signUp(name, email, password)){
                    continue;
                }
            }
            else {
                System.out.println("Invalid option. Please enter [L]ogin or [S]ign up.");
                continue;
            }

            if (user != null){
                System.out.println("Welcome back, " + user.getName() + "!");
                System.out.print("[S]end, [V]iew, [R]eply, [F]orward: ");
                String command = scanner.nextLine().toLowerCase();

                if (command.equals("s") || command.equals("send")){
                    System.out.print("Recipient(s) (comma-separated): ");
                    String recipientInput = scanner.nextLine();
                    List<String> recipient = Arrays.asList(recipientInput.split(","));

                    System.out.print("Subject: ");
                    String subject = scanner.nextLine();

                    System.out.print("Body: ");
                    String body = scanner.nextLine();

                    EmailService.sendEmail(user, recipient, subject, body);
                }
            }
        }
    }
}