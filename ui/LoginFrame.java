package ui;

import model.User;
import service.UserService;

import javax.swing.*;
import java.awt.*;

import service.DoctorApprovalService;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private UserService userService;
    private DoctorApprovalService doctorApprovalService;

    public LoginFrame() {
        userService = new UserService();

        setTitle("QueueCare - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        doctorApprovalService = new DoctorApprovalService();

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("QueueCare");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;

        JLabel roleLabel = new JLabel("Login As:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(roleLabel, gbc);

        roleBox = new JComboBox<>(
                new String[] { "ADMIN", "DOCTOR", "PATIENT" });

        gbc.gridx = 1;
        mainPanel.add(roleBox, gbc);

        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(emailLabel, gbc);

        emailField = new JTextField();
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> login());

        JButton registerButton = new JButton("Register");
        gbc.gridy = 5;
        mainPanel.add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            dispose();
            new RegisterFrame();
        });

        add(mainPanel);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = roleBox.getSelectedItem().toString();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter email and password.",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userService.login(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!user.getRole().equalsIgnoreCase(selectedRole)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selected role does not match your account.",
                    "Role Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (user.getRole().equalsIgnoreCase("DOCTOR")) {
            if (!doctorApprovalService.isApproved(user.getId())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Your doctor account is waiting for admin approval.",
                        "Account Pending",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        dispose();

        if (user.getRole().equalsIgnoreCase("ADMIN")) {
            new AdminDashboard(user);
        } else if (user.getRole().equalsIgnoreCase("DOCTOR")) {
            new DoctorDashboard(user);
        } else if (user.getRole().equalsIgnoreCase("PATIENT")) {
            new PatientDashboard(user);
        }
    }
}