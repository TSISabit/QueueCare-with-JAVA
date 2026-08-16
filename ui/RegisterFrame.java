package ui;

import javax.swing.*;

import service.UserService;

import java.awt.*;
import model.Doctor;
import model.Patient;
import service.UserService;
import service.DoctorApprovalService;

public class RegisterFrame extends JFrame {
    private JComboBox<String> roleBox;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField phoneField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField addressField;
    private JTextField specializationField;
    private JTextField feeField;
    private UserService userService;
    private JPanel extraPanel;
    private DoctorApprovalService doctorApprovalService;

    public RegisterFrame() {
        userService = new UserService();
        setTitle("QueueCare - Register");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        doctorApprovalService = new DoctorApprovalService();

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Register As:"), gbc);

        roleBox = new JComboBox<>(new String[] { "PATIENT", "DOCTOR" });
        gbc.gridx = 1;
        formPanel.add(roleBox, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Name:"), gbc);

        nameField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Email:"), gbc);

        emailField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField();
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        row++;

        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Phone:"), gbc);

        phoneField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);
        row++;

        extraPanel = new JPanel(new GridBagLayout());
        addExtraFields();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(extraPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        roleBox.addActionListener(e -> addExtraFields());

        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        add(mainPanel);
    }

    private void addExtraFields() {
        extraPanel.removeAll();

        String role = roleBox.getSelectedItem().toString();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        if (role.equals("PATIENT")) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            extraPanel.add(new JLabel("Age:"), gbc);

            ageField = new JTextField();
            gbc.gridx = 1;
            extraPanel.add(ageField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            extraPanel.add(new JLabel("Gender:"), gbc);

            genderField = new JTextField();
            gbc.gridx = 1;
            extraPanel.add(genderField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            extraPanel.add(new JLabel("Address:"), gbc);

            addressField = new JTextField();
            gbc.gridx = 1;
            extraPanel.add(addressField, gbc);
        } else {
            gbc.gridx = 0;
            gbc.gridy = 0;
            extraPanel.add(new JLabel("Specialization:"), gbc);

            specializationField = new JTextField();
            gbc.gridx = 1;
            extraPanel.add(specializationField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            extraPanel.add(new JLabel("Consultation Fee:"), gbc);

            feeField = new JTextField();
            gbc.gridx = 1;
            extraPanel.add(feeField, gbc);
        }

        extraPanel.revalidate();
        extraPanel.repaint();
        pack();
        setLocationRelativeTo(null);
    }

    private void register() {
        String role = roleBox.getSelectedItem().toString();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            showError("Please fill all required fields.");
            return;
        }

        if (!email.contains("@")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (userService.findUserByEmail(email) != null) {
            showError("An account with this email already exists.");
            return;
        }

        String id = userService.generateId(role);

        if (role.equals("PATIENT")) {
            if (ageField.getText().trim().isEmpty() ||
                    genderField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty()) {
                showError("Please fill all patient information.");
                return;
            }

            try {
                int age = Integer.parseInt(ageField.getText().trim());

                Patient patient = new Patient(
                        id,
                        name,
                        email,
                        password,
                        age,
                        genderField.getText().trim(),
                        phone,
                        addressField.getText().trim());

                if (userService.registerUser(patient)) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Patient account created successfully.\nYour ID: " + id);
                    dispose();
                    new LoginFrame();
                } else {
                    showError("Registration failed.");
                }
            } catch (NumberFormatException e) {
                showError("Age must be a number.");
            }
        } else {
            if (specializationField.getText().trim().isEmpty() ||
                    feeField.getText().trim().isEmpty()) {
                showError("Please fill all doctor information.");
                return;
            }

            try {
                double fee = Double.parseDouble(feeField.getText().trim());

                Doctor doctor = new Doctor(
                        id,
                        name,
                        email,
                        password,
                        phone,
                        specializationField.getText().trim(),
                        fee);

                if (userService.registerUser(doctor)) {
                    doctorApprovalService.createPendingRequest(doctor);

                    JOptionPane.showMessageDialog(
                            this,
                            "Doctor registration submitted.\nYour ID: " + id +
                                    "\n\nWaiting for admin approval.");

                    dispose();
                    new LoginFrame();
                } else {
                    showError("Registration failed.");
                }
            } catch (NumberFormatException e) {
                showError("Consultation fee must be a number.");
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
    }
}