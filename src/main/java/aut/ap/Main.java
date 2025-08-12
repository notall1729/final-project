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
    static User user = null;

    public static void main(String[] args) {


        JFrame frame = new JFrame("milou!");
        JPanel mainPanel = new JPanel();
        frame.setBounds(0, 0, 750, 480);
        mainPanel.setLayout(null);


        ImageIcon background = new ImageIcon("C:\\Users\\MY-PC\\final-project\\src\\main\\resources\\background.png");
        JLabel backgroundLabel = new JLabel(background);
        backgroundLabel.setBounds(0, 0, 750, 480);

        JLabel welcome = new JLabel("Welcome to Milou!");
        welcome.setBounds(310, 60, 140, 40);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));

        JButton signupButton = new JButton("Sign up");
        signupButton.setBounds(320, 180, 110, 40);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(320, 240, 110, 40);
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
                emailField.setBounds(320, 150, 140, 40);
                mainPanel.add(emailField);

                JLabel passwordLabel = new JLabel("Password:");
                passwordLabel.setBounds(250, 210, 110, 40);
                mainPanel.add(passwordLabel);

                JTextField passwordField = new JTextField();
                passwordField.setBounds(320, 210, 140, 40);
                mainPanel.add(passwordField);

                JButton nextButton = new JButton("Next");
                nextButton.setBounds(340, 260, 90, 30);
                mainPanel.add(nextButton);

                JButton goBack = new JButton("Go back");
                goBack.setBounds(340, 300, 90, 30);
                mainPanel.add(goBack);
                goBack.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.removeAll();
                        mainPanel.setLayout(null);
                        mainPanel.add(backgroundLabel);

                        mainPanel.add(loginButton);
                        mainPanel.add(signupButton);
                        mainPanel.add(welcome);

                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });

                nextButton.addActionListener(ev -> {
                    String email = emailField.getText();
                    String password = passwordField.getText();

                    user = AuthService.login(email, password);

                    if (user == null) {
                        JLabel loginError = new JLabel("Error: Invalid email or password!");
                        loginError.setBounds(300, 340, 250, 30);
                        loginError.setForeground(Color.RED);
                        mainPanel.add(loginError);
                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    } else {
                        mainPanel.removeAll();
                        mainPanel.setLayout(null);
                        mainPanel.add(backgroundLabel);
                        JLabel welcomeMessage = new JLabel("Welcome back, " + user.getName() + "!");
                        welcomeMessage.setBounds(290, 100, 180, 30);
                        welcomeMessage.setFont(new Font("Arial", Font.BOLD, 16));
                        mainPanel.add(welcomeMessage);

                        JButton sendButton = new JButton("Send");
                        JButton viewButton = new JButton("View");
                        JButton replyButton = new JButton("Reply");
                        JButton forwardButton = new JButton("Forward");
                        JButton goBackToMenu = new JButton("Go back");
                        goBackToMenu.setBounds(320, 320, 90, 30);
                        goBackToMenu.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                mainPanel.add(sendButton);
                                mainPanel.add(viewButton);
                                mainPanel.add(replyButton);
                                mainPanel.add(forwardButton);
                                mainPanel.add(welcomeMessage);
                                mainPanel.add(goBackToMenu);

                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });

                        JButton goBackToLogin = new JButton("Go back");
                        goBackToLogin.setBounds(340, 320, 90, 30);
                        goBackToLogin.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                mainPanel.add(passwordField);
                                mainPanel.add(passwordLabel);
                                mainPanel.add(emailField);
                                mainPanel.add(emailLabel);
                                mainPanel.add(goBack);
                                mainPanel.add(nextButton);

                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });
                        mainPanel.add(goBackToLogin);

                        sendButton.setBounds(290, 140, 80, 40);
                        mainPanel.add(sendButton);
                        sendButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                JLabel recipientsLabel = new JLabel("Recipient(s) (comma-separated): ");
                                recipientsLabel.setBounds(50, 120, 190, 30);
                                mainPanel.add(recipientsLabel);

                                JTextField recipientsField = new JTextField();
                                recipientsField.setBounds(240, 120, 150, 40);
                                mainPanel.add(recipientsField);

                                JLabel subjectLabel = new JLabel("Subject: ");
                                subjectLabel.setBounds(185, 170, 50, 30);
                                mainPanel.add(subjectLabel);

                                JTextField subjectField = new JTextField();
                                subjectField.setBounds(240, 170, 150, 40);
                                mainPanel.add(subjectField);

                                JLabel bodyLabel = new JLabel("Body: ");
                                bodyLabel.setBounds(200, 220, 50, 30);
                                mainPanel.add(bodyLabel);

                                JTextField bodyField = new JTextField();
                                bodyField.setBounds(240, 220, 450, 80);
                                mainPanel.add(bodyField);

                                mainPanel.add(goBackToLogin);

                                JButton sendEmail = new JButton("Send");
                                sendEmail.setBounds(340, 370, 90, 30);

                                mainPanel.add(sendEmail);
                                sendEmail.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String recipient = recipientsField.getText();
                                        List<String> recipients = Arrays.asList(recipient.split(","));
                                        String subject = subjectField.getText();
                                        String body = bodyField.getText();

                                        String result = EmailService.sendEmail(user, recipients, subject, body);

                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        JLabel message = new JLabel(result);
                                        message.setBounds(220, 170, 380, 100);
                                        message.setFont(new Font("Arial", Font.BOLD, 20));

                                        mainPanel.add(goBackToMenu);

                                        if (result.startsWith("Error")) {
                                            message.setForeground(Color.RED);
                                        } else {
                                            message.setForeground(Color.GREEN);
                                        }
                                        mainPanel.add(message);

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });

                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });


                        viewButton.setBounds(290, 200, 80, 40);
                        mainPanel.add(viewButton);
                        viewButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                JButton viewAll = new JButton("All emails");
                                JButton viewUnReads = new JButton("Unreads emails");
                                JButton readByCode = new JButton("Read by code");
                                JButton viewSent = new JButton("Sent emails");

                                viewAll.setBounds(240, 170, 130, 50);
                                mainPanel.add(viewAll);
                                mainPanel.add(goBackToMenu);
                                viewAll.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        String result = EmailService.viewEmails(user, "a");

                                        JLabel emails = new JLabel(result);
                                        emails.setBounds(200, 50, 500, 350);
                                        emails.setFont(new Font("Arial", Font.BOLD, 18));
                                        mainPanel.add(emails);

                                        JButton goBackToView = new JButton("Go back");
                                        goBackToView.setBounds(200, 300, 80, 40);
                                        mainPanel.add(goBackToView);
                                        goBackToView.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                mainPanel.removeAll();
                                                mainPanel.setLayout(null);
                                                mainPanel.add(backgroundLabel);

                                                mainPanel.add(viewAll);
                                                mainPanel.add(viewSent);
                                                mainPanel.add(viewUnReads);
                                                mainPanel.add(readByCode);
                                                mainPanel.add(goBackToMenu);

                                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                                mainPanel.revalidate();
                                                mainPanel.repaint();
                                            }
                                        });
                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });

                                viewUnReads.setBounds(390, 170, 130, 50);
                                mainPanel.add(viewUnReads);

                                viewUnReads.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        String result = EmailService.viewEmails(user, "u");

                                        JLabel emails = new JLabel(result);
                                        emails.setBounds(200, 50, 500, 350);
                                        emails.setFont(new Font("Arial", Font.BOLD, 18));
                                        mainPanel.add(emails);

                                        JButton goBackToView = new JButton("Go back");
                                        goBackToView.setBounds(200, 300, 80, 40);
                                        mainPanel.add(goBackToView);
                                        goBackToView.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                mainPanel.removeAll();
                                                mainPanel.setLayout(null);
                                                mainPanel.add(backgroundLabel);

                                                mainPanel.add(viewAll);
                                                mainPanel.add(viewSent);
                                                mainPanel.add(viewUnReads);
                                                mainPanel.add(readByCode);
                                                mainPanel.add(goBackToMenu);

                                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                                mainPanel.revalidate();
                                                mainPanel.repaint();
                                            }
                                        });
                                        mainPanel.add(goBackToView);

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });

                                readByCode.setBounds(240, 240, 130, 50);
                                mainPanel.add(readByCode);
                                readByCode.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        JLabel codeLabel = new JLabel("Code:");
                                        codeLabel.setBounds(250, 200, 80, 40);
                                        mainPanel.add(codeLabel);

                                        JTextField codeField = new JTextField();
                                        codeField.setBounds(300, 200, 100, 40);
                                        mainPanel.add(codeField);

                                        JButton next = new JButton("Next");
                                        next.setBounds(300, 300, 80, 40);
                                        mainPanel.add(next);
                                        
                                        next.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                mainPanel.removeAll();
                                                mainPanel.setLayout(null);
                                                mainPanel.add(backgroundLabel);

                                                String code = codeField.getText();
                                                String result = EmailService.readEmail(user, code);

                                                JLabel emails = new JLabel(result);
                                                emails.setBounds(200, 10, 600, 350);
                                                emails.setFont(new Font("Arial", Font.BOLD, 18));
                                                mainPanel.add(emails);

                                                JButton goBackToView = new JButton("Go back");
                                                goBackToView.setBounds(200, 360, 80, 40);
                                                mainPanel.add(goBackToView);
                                                goBackToView.addActionListener(new ActionListener() {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e) {
                                                        mainPanel.removeAll();
                                                        mainPanel.setLayout(null);
                                                        mainPanel.add(backgroundLabel);

                                                        mainPanel.add(viewAll);
                                                        mainPanel.add(viewSent);
                                                        mainPanel.add(viewUnReads);
                                                        mainPanel.add(readByCode);
                                                        mainPanel.add(goBackToMenu);

                                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                                        mainPanel.revalidate();
                                                        mainPanel.repaint();
                                                    }
                                                });
                                                mainPanel.add(goBackToView);

                                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                                mainPanel.revalidate();
                                                mainPanel.repaint();
                                            }
                                        });

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });


                                viewSent.setBounds(390, 240, 130, 50);
                                mainPanel.add(viewSent);
                                viewSent.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        String result = EmailService.viewEmails(user, "s");

                                        JLabel emails = new JLabel(result);
                                        emails.setBounds(200, 10, 500, 350);
                                        emails.setFont(new Font("Arial", Font.BOLD, 18));
                                        mainPanel.add(emails);

                                        JButton goBackToView = new JButton("Go back");
                                        goBackToView.setBounds(200, 360, 80, 40);
                                        mainPanel.add(goBackToView);
                                        goBackToView.addActionListener(new ActionListener() {
                                            @Override
                                            public void actionPerformed(ActionEvent e) {
                                                mainPanel.removeAll();
                                                mainPanel.setLayout(null);
                                                mainPanel.add(backgroundLabel);

                                                mainPanel.add(viewAll);
                                                mainPanel.add(viewSent);
                                                mainPanel.add(viewUnReads);
                                                mainPanel.add(readByCode);
                                                mainPanel.add(goBackToMenu);

                                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                                mainPanel.revalidate();
                                                mainPanel.repaint();
                                            }
                                        });
                                        mainPanel.add(goBackToView);

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });

                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });

                        replyButton.setBounds(390, 140, 80, 40);
                        mainPanel.add(replyButton);
                        replyButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                JLabel codeLabel = new JLabel("Code:");
                                codeLabel.setBounds(230, 160, 80, 40);
                                mainPanel.add(codeLabel);

                                JLabel bodyLabel = new JLabel("Body:");
                                bodyLabel.setBounds(230, 210, 80, 40);
                                mainPanel.add(bodyLabel);

                                JTextField codeField = new JTextField();
                                codeField.setBounds(280, 160, 80, 40);
                                mainPanel.add(codeField);

                                JTextField bodyField = new JTextField();
                                bodyField.setBounds(280, 210, 320, 50);
                                mainPanel.add(bodyField);

                                JButton sendReply = new JButton("send");
                                sendReply.setBounds(320, 280, 90, 30);
                                sendReply.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        String code = codeField.getText();
                                        String body = bodyField.getText();

                                        String result = EmailService.replyToEmail(user, code, body);
                                        JLabel message = new JLabel(result);
                                        message.setBounds(200, 150, 350, 120);
                                        mainPanel.add(message);
                                        mainPanel.add(goBackToMenu);

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });
                                mainPanel.add(sendReply);
                                mainPanel.add(goBackToMenu);


                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });

                        forwardButton.setBounds(390, 200, 80, 40);
                        mainPanel.add(forwardButton);
                        forwardButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                mainPanel.removeAll();
                                mainPanel.setLayout(null);
                                mainPanel.add(backgroundLabel);

                                JLabel codeLabel = new JLabel("Code:");
                                codeLabel.setBounds(230, 160, 80, 40);
                                mainPanel.add(codeLabel);

                                JLabel recipientsLabel = new JLabel("recipient(s) (comma-separated): ");
                                recipientsLabel.setBounds(90, 210, 200, 40);
                                mainPanel.add(recipientsLabel);

                                JTextField codeField = new JTextField();
                                codeField.setBounds(280, 160, 80, 40);
                                mainPanel.add(codeField);

                                JTextField recipientsField = new JTextField();
                                recipientsField.setBounds(280, 210, 200, 50);
                                mainPanel.add(recipientsField);

                                mainPanel.add(goBackToMenu);
                                JButton sendForward = new JButton("Send");
                                sendForward.setBounds(320, 280, 90, 30);
                                mainPanel.add(sendForward);
                                sendForward.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        mainPanel.removeAll();
                                        mainPanel.setLayout(null);
                                        mainPanel.add(backgroundLabel);

                                        String code = codeField.getText();
                                        String recipient = recipientsField.getText();
                                        List<String> recipients = Arrays.asList(recipient.split(","));

                                        String result = EmailService.forwardEmail(user, code, recipients);

                                        JLabel message = new JLabel(result);
                                        message.setBounds(250, 150, 350, 90);
                                        mainPanel.add(message);
                                        mainPanel.add(goBackToMenu);

                                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                        mainPanel.revalidate();
                                        mainPanel.repaint();
                                    }
                                });

                                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                                mainPanel.revalidate();
                                mainPanel.repaint();
                            }
                        });

                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });

                mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });


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

                JButton goBack = new JButton("Go back");
                goBack.setBounds(340, 300, 90, 30);
                mainPanel.add(goBack);
                goBack.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.removeAll();
                        mainPanel.setLayout(null);
                        mainPanel.add(backgroundLabel);

                        mainPanel.add(loginButton);
                        mainPanel.add(signupButton);
                        mainPanel.add(welcome);

                        mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                });

                JButton nextButton = new JButton("Next");
                nextButton.setBounds(340, 260, 90, 30);
                mainPanel.add(nextButton);
                nextButton.addActionListener(ev -> {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String password = passwordField.getText();

                    String result = AuthService.signUp(name, email, password);
                    if (!result.equals("true")) {
                        JLabel error = new JLabel(result);
                        error.setBounds(250, 340, 400, 50);
                        mainPanel.add(error);

                        Timer timer = new Timer(10000, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                error.setVisible(false);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        JLabel massage = new JLabel("Your new account is created. Go ahead and login!");
                        massage.setBounds(250, 330, 490, 50);
                        massage.setForeground(Color.GREEN);
                        mainPanel.add(massage);
                    }
                    mainPanel.setComponentZOrder(backgroundLabel, mainPanel.getComponentCount() - 1);
                    mainPanel.revalidate();
                    mainPanel.repaint();
                });
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
    }
}