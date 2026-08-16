package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private User loggedInUser;

    public AdminDashboard(User user) {

        this.loggedInUser = user;

        setTitle("QueueCare - Admin Dashboard");

        setSize(800, 500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        createUI();

        setVisible(true);
    }

    private void createUI() {
        // MAIN PANEL

        JPanel mainPanel = new JPanel(new BorderLayout());

        // HEADER

        JPanel headerPanel = new JPanel(new BorderLayout());

        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("QueueCare");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getName());

        headerPanel.add(titleLabel, BorderLayout.WEST);

        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // SIDEBAR

        JPanel sidebar = new JPanel();

        sidebar.setLayout(new GridLayout(5, 1, 10, 10));

        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton doctorButton = new JButton("Manage Doctors");

        JButton patientButton = new JButton("Manage Patients");

        JButton appointmentButton = new JButton("View Appointments");

        JButton logoutButton = new JButton("Logout");

        JButton doctorApprovalButton = new JButton("Doctor Approval Requests");

        sidebar.add(doctorButton);
        sidebar.add(patientButton);
        sidebar.add(appointmentButton);
        sidebar.add(logoutButton);
        sidebar.add(doctorApprovalButton);

        mainPanel.add(sidebar, BorderLayout.WEST);

        // CONTENT

        JPanel contentPanel = new JPanel(new GridBagLayout());

        JLabel dashboardLabel = new JLabel("Admin Dashboard");

        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 30));

        contentPanel.add(dashboardLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // BUTTON ACTIONS

        doctorButton.addActionListener(e -> new DoctorManagementFrame());

        patientButton.addActionListener(e -> new PatientManagementFrame());

        appointmentButton.addActionListener(e -> new AppointmentManagementFrame());

        logoutButton.addActionListener(e -> logout());

        add(mainPanel);
    }

    // LOGOUT

    private void logout() {

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            dispose();

            new LoginFrame();
        }
    }
}