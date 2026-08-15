package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private UserService userService;


    public LoginFrame() {

        userService = new UserService();

        setTitle("QueueCare - Login");

        setSize(450, 350);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        createUI();

        setVisible(true);
    }


    private void createUI() {
        // Main panel
        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        mainPanel.setBorder(BorderFactory.createEmptyBorder(
                        25, 35, 25, 35));

        // TITLE

        JLabel titleLabel = new JLabel("QueueCare", SwingConstants.CENTER);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        JLabel subtitleLabel = new JLabel("Hospital Appointment Management", SwingConstants.CENTER);

        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1));

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);


        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // FORM

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 5, 5));

        JLabel emailLabel = new JLabel("Email:");

        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");

        passwordField = new JPasswordField();


        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // LOGIN BUTTON

        loginButton = new JButton("LOGIN");

        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        loginButton.addActionListener(e -> login());

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(loginButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // LOGIN LOGIC

    private void login() {

        String email = emailField.getText().trim();

        String password = new String(passwordField.getPassword());

        // Empty field validation
        if (email.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please enter email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate user
        User user = userService.login(email, password);

        if (user != null) {

            JOptionPane.showMessageDialog(this, "Welcome, " + user.getName() + "!");

            System.out.println("Logged in as: " + user.getRole());

        } else {

            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}