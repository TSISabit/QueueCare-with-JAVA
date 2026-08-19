package ui;

import model.Patient;
import service.PatientService;
import service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientManagementFrame extends JFrame {
    private PatientService patientService;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private UserService userService;

    public PatientManagementFrame() {
        patientService = new PatientService();
        userService = new UserService();

        setTitle("QueueCare - Manage Patients");
        setSize(950, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadPatients();

        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Manage Patients");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = { "ID", "Name", "Email", "Age", "Gender", "Phone", "Address" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(patientTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("Add Patient");
        JButton updateButton = new JButton("Update Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton searchButton = new JButton("Search");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        searchButton.addActionListener(e -> searchPatient());
        refreshButton.addActionListener(e -> loadPatients());

        add(mainPanel);
    }

    private void loadPatients() {
        tableModel.setRowCount(0);

        List<Patient> patients = patientService.getAllPatients();

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getPhone(),
                    patient.getAddress()
            };

            tableModel.addRow(row);
        }
    }

    private void addPatient() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));

        panel.add(new JLabel("Patient ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Patient",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = genderField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || gender.isEmpty() ||
                    phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.",
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = new Patient(id, name, email, password,
                    age, gender, phone, address);

            boolean added = patientService.addPatient(patient);

            if (added) {
                JOptionPane.showMessageDialog(this, "Patient added successfully.");
                loadPatients();
            } else {
                JOptionPane.showMessageDialog(this, "Patient ID already exists.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a patient first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String currentName = tableModel.getValueAt(selectedRow, 1).toString();
        String currentEmail = tableModel.getValueAt(selectedRow, 2).toString();
        String currentAge = tableModel.getValueAt(selectedRow, 3).toString();
        String currentGender = tableModel.getValueAt(selectedRow, 4).toString();
        String currentPhone = tableModel.getValueAt(selectedRow, 5).toString();
        String currentAddress = tableModel.getValueAt(selectedRow, 6).toString();

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField ageField = new JTextField(currentAge);
        JTextField genderField = new JTextField(currentGender);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField addressField = new JTextField(currentAddress);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Patient",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = genderField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty() ||
                    gender.isEmpty() || phone.isEmpty() ||
                    address.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean patientUpdated = patientService.updatePatient(
                    id,
                    name,
                    email,
                    age,
                    gender,
                    phone,
                    address);

            if (!patientUpdated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Patient could not be updated.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean userUpdated = userService.updateUser(
                    id,
                    name,
                    email);

            if (!userUpdated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Patient updated, but users.csv could not be updated.",
                        "Synchronization Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Patient updated successfully.");

            loadPatients();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Age must be a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a patient first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String name = tableModel.getValueAt(selectedRow, 1).toString();

        int confirmation = JOptionPane.showConfirmDialog(
                this,
                "Delete patient " + name + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean patientDeleted = patientService.deletePatient(id);

        if (!patientDeleted) {
            JOptionPane.showMessageDialog(
                    this,
                    "Patient could not be deleted.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean userDeleted = userService.deleteUser(id);

        if (!userDeleted) {
            JOptionPane.showMessageDialog(
                    this,
                    "Patient deleted, but users.csv could not be updated.",
                    "Synchronization Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Patient deleted successfully.");

        loadPatients();
    }

    private void searchPatient() {
        String keyword = JOptionPane.showInputDialog(this, "Enter patient ID or name:");

        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }

        List<Patient> patients = patientService.searchPatient(keyword.trim());

        tableModel.setRowCount(0);

        for (Patient patient : patients) {
            Object[] row = {
                    patient.getId(),
                    patient.getName(),
                    patient.getEmail(),
                    patient.getAge(),
                    patient.getGender(),
                    patient.getPhone(),
                    patient.getAddress()
            };
            tableModel.addRow(row);
        }
    }
}