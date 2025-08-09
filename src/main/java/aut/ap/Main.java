package aut.ap;

import aut.ap.services.AuthService;
import aut.ap.services.EmailService;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        JFrame frame = new JFrame("Welcome to milou!");
        JPanel mainPanel = new JPanel();

        ImageIcon imageIcon = new ImageIcon("")
        frame.setSize(800, 600);
        frame.add(mainPanel);
        frame.setVisible(true);
        while (true){
            System.out.println("[L]ogin, [S]ign up: ");
            String input = scanner.nextLine().toLowerCase();

            User user = null;

            if (input.equals("l") || input.equals("login")){
                System.out.println("Email: ");
                String email = scanner.nextLine();

                System.out.println("Password: ");
                String password = scanner.nextLine();

               user = AuthService.login(email, password);
            }
            else if (input.equals("s") || input.equals("sign up")){
                System.out.println("Name: ");
                String name = scanner.nextLine().trim();

                System.out.println("Email: ");
                String email = scanner.nextLine().trim();

                System.out.println("Password: ");
                String password = scanner.nextLine().trim();

                if(AuthService.signUp(name, email, password)){
                    printLine();
                    continue;
                }
            }
            else {
                System.out.println("Invalid option. Please enter [L]ogin or [S]ign up.");
                continue;
            }

            if (user != null) {
                printLine();
                System.out.println("Welcome back, " + user.getName() + "!");
                while (true) {
                    printLine();
                    System.out.println("[S]end, [V]iew, [R]eply, [F]orward: ");
                    String command = scanner.nextLine().toLowerCase();

                    if (command.equals("s") || command.equals("send")) {
                        System.out.println("Recipient(s) (comma-separated): ");
                        String recipientInput = scanner.nextLine();
                        List<String> recipient = Arrays.asList(recipientInput.split(","));

                        System.out.println("Subject: ");
                        String subject = scanner.nextLine();

                        System.out.println("Body: ");
                        String body = scanner.nextLine();

                        EmailService.sendEmail(user, recipient, subject, body);

                    } else if (command.equals("v") || command.equals("view")) {
                        printLine();
                        System.out.println("[A]ll emails, [U]nread emails, [S]ent emails, Read by [C]ode: ");
                        String newCommand = scanner.nextLine().toLowerCase();

                        if (newCommand.equals("a") || newCommand.equals("u") || newCommand.equals("s")) {
                            EmailService.viewEmails(user, newCommand);
                        } else if (newCommand.equals("c")) {
                            System.out.println("Code: ");
                            String emailCode = scanner.nextLine();
                            EmailService.readEmail(user, emailCode);
                        } else {
                            System.out.println("Invalid option.");
                        }
                    } else if (command.equals("r") || command.equals("reply")) {
                        System.out.println("Code: ");
                        String code = scanner.nextLine();

                        System.out.println("Body: ");
                        String body = scanner.nextLine();

                        EmailService.replyToEmail(user, code, body);
                    } else if (command.equals("f") || command.equals("forward")){
                        System.out.println("Code: ");
                        String code = scanner.nextLine();

                        System.out.println("Recipient(s) (comma-separated): ");
                        String recipientInput = scanner.nextLine();
                        List<String> recipient = Arrays.asList(recipientInput.split(","));

                        System.out.println("Extra note: ");
                        String note = scanner.nextLine();

                        EmailService.forwardEmail(user, code, recipient);
                    }
                }
            }
        }
    }

    public static void printLine(){
        System.out.println();
        System.out.println("-----------------------------------------------------");
        System.out.println();
    }
}