package ui;

import model.Doctor;
import service.DoctorService;
import service.UserService;
import service.DoctorApprovalService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorManagementFrame extends JFrame {

    private DoctorService doctorService;
    private UserService userService;
    private DoctorApprovalService doctorApprovalService;

    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorManagementFrame() {
        doctorService = new DoctorService();
        userService = new UserService();
        doctorApprovalService = new DoctorApprovalService();

        setTitle("QueueCare - Manage Doctors");

        setSize(900, 500);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);

        createUI();

        loadDoctors();

        setVisible(true);
    }

    // CREATE UI

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // TITLE

        JLabel titleLabel = new JLabel("Manage Doctors");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // TABLE

        String[] columns = {
                "ID",
                "Name",
                "Email",
                "Phone",
                "Specialization",
                "Fee"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);

        doctorTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(doctorTable);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // BUTTONS

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("Add Doctor");

        JButton updateButton = new JButton("Update Doctor");

        JButton deleteButton = new JButton("Delete Doctor");

        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // BUTTON ACTIONS

        addButton.addActionListener(e -> addDoctor());

        updateButton.addActionListener(e -> updateDoctor());

        deleteButton.addActionListener(e -> deleteDoctor());

        refreshButton.addActionListener(e -> loadDoctors());

        add(mainPanel);
    }

    // LOAD DOCTORS

    private void loadDoctors() {

        tableModel.setRowCount(0);

        List<Doctor> doctors = doctorService.getAllDoctors();

        for (Doctor doctor : doctors) {

            Object[] row = {

                    doctor.getId(),

                    doctor.getName(),

                    doctor.getEmail(),

                    doctor.getPhone(),

                    doctor.getSpecialization(),

                    doctor.getConsultationFee()
            };

            tableModel.addRow(row);
        }
    }

    // ADD DOCTOR

    private void addDoctor() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField phoneField = new JTextField();
        JTextField specializationField = new JTextField();
        JTextField feeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));

        panel.add(new JLabel("Doctor ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Consultation Fee:"));
        panel.add(feeField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Doctor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String phone = phoneField.getText().trim();
            String specialization = specializationField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || phone.isEmpty() ||
                    specialization.isEmpty() || feeField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userService.findUserById(id) != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "This Doctor ID already exists.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userService.findUserByEmail(email) != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "An account with this email already exists.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            double fee = Double.parseDouble(feeField.getText().trim());

            Doctor doctor = new Doctor(
                    id,
                    name,
                    email,
                    password,
                    phone,
                    specialization,
                    fee);

            boolean userAdded = userService.registerUser(doctor);

            if (!userAdded) {
                JOptionPane.showMessageDialog(
                        this,
                        "Could not create Doctor account.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean doctorAdded = doctorService.addDoctor(doctor);

            if (!doctorAdded) {
                JOptionPane.showMessageDialog(
                        this,
                        "Doctor account was created, but Doctor profile could not be added.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            doctorApprovalService.createPendingRequest(doctor);
            doctorApprovalService.updateStatus(id, "APPROVED");

            JOptionPane.showMessageDialog(
                    this,
                    "Doctor added and approved successfully.");

            loadDoctors();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Consultation fee must be a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE DOCTOR

    private void updateDoctor() {
        int selectedRow = doctorTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();
        String currentName = tableModel.getValueAt(selectedRow, 1).toString();
        String currentEmail = tableModel.getValueAt(selectedRow, 2).toString();
        String currentPhone = tableModel.getValueAt(selectedRow, 3).toString();
        String currentSpecialization = tableModel.getValueAt(selectedRow, 4).toString();
        String currentFee = tableModel.getValueAt(selectedRow, 5).toString();

        JTextField nameField = new JTextField(currentName);
        JTextField emailField = new JTextField(currentEmail);
        JTextField phoneField = new JTextField(currentPhone);
        JTextField specializationField = new JTextField(currentSpecialization);
        JTextField feeField = new JTextField(currentFee);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Specialization:"));
        panel.add(specializationField);
        panel.add(new JLabel("Consultation Fee:"));
        panel.add(feeField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Update Doctor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String specialization = specializationField.getText().trim();
            double fee = Double.parseDouble(feeField.getText().trim());

            if (name.isEmpty() || email.isEmpty() ||
                    phone.isEmpty() || specialization.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean doctorUpdated = doctorService.updateDoctor(
                    id,
                    name,
                    email,
                    phone,
                    specialization,
                    fee);

            if (!doctorUpdated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Doctor could not be updated.",
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
                        "Doctor updated, but users.csv could not be updated.",
                        "Synchronization Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Doctor updated successfully.");

            loadDoctors();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Consultation fee must be a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE DOCTOR

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a doctor first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = tableModel.getValueAt(selectedRow, 0).toString();

        String name = tableModel.getValueAt(selectedRow, 1).toString();

        int confirmation = JOptionPane.showConfirmDialog(this, "Delete Dr. " + name + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted = doctorService.deleteDoctor(id);

        if (deleted) {
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully.");

            loadDoctors();

        } else {
            JOptionPane.showMessageDialog(this, "Doctor could not be deleted.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}