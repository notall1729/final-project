package aut.ap;

import aut.ap.services.AuthService;
import aut.ap.services.EmailService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        JFrame frame = new JFrame("milou!");
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);


        ImageIcon background = new ImageIcon("C:\\Users\\MY-PC\\final-project\\src\\main\\resources\\background.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 750, 480);

        JLabel welcome = new JLabel("Welcome to Milou!");
        welcome.setBounds(310, 60, 140, 40);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(320, 240,110, 40);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.setLayout(null);
                mainPanel.add(backgroundLabel);

                JLabel emailLabel = new JLabel("Email:");
                emailLabel.setBounds(250, 150, 110, 40);
                mainPanel.add(emailLabel);

                JTextField emailField = new JTextField();
                emailField.setBounds(320, 210, 140, 40);
                mainPanel.add(emailField);

                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setBounds(250, 210, 110, 40);
                mainPanel.add(passwordLabel);

                JTextField passwordField = new JTextField();
                passwordField.setBounds(320, 150, 140, 40);
                mainPanel.add(passwordField);

                JButton nextButton = new JButton("Next");
                nextButton.setBounds(340, 280, 90, 30);
                mainPanel.add(nextButton);

                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        JButton signupButton = new JButton("Sign up");
        signupButton.setBounds(320,180, 110, 40);
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.removeAll();
                mainPanel.setLayout(null);
                mainPanel.add(backgroundLabel);

                JLabel nameLabel = new JLabel("Name:");
                nameLabel.setBounds(250, 90, 110, 40);
                mainPanel.add(nameLabel);

                JTextField nameField = new JTextField();
                nameField.setBounds(320, 90, 140, 40);
                mainPanel.add(nameField);

                JLabel emailLabel = new JLabel("Email:");
                emailLabel.setBounds(250, 150, 110, 40);
                mainPanel.add(emailLabel);

                JTextField emailField = new JTextField();
                emailField.setBounds(320, 210, 140, 40);
                mainPanel.add(emailField);

                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setBounds(250, 210, 110, 40);
                mainPanel.add(passwordLabel);

                JTextField passwordField = new JTextField();
                passwordField.setBounds(320, 150, 140, 40);
                mainPanel.add(passwordField);

                JButton nextButton = new JButton("Next");
                nextButton.setBounds(340, 280, 90, 30);
                mainPanel.add(nextButton);

                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        mainPanel.add(welcome);
        mainPanel.add(loginButton);
        mainPanel.add(signupButton);
        mainPanel.add(backgroundLabel);

        frame.setBounds(0, 0, 750, 480);
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