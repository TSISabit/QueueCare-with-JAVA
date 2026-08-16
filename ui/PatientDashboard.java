package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class PatientDashboard extends JFrame {
    private User loggedInUser;

    public PatientDashboard(User user) {
        loggedInUser = user;

        setTitle("QueueCare - Patient Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("QueueCare");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUser.getName());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel sidebar = new JPanel(new GridLayout(5, 1, 10, 10));

        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JButton profileButton = new JButton("My Profile");
        JButton doctorsButton = new JButton("Find Doctor");
        JButton bookButton = new JButton("Book Appointment");
        JButton appointmentsButton = new JButton("My Appointments");
        JButton logoutButton = new JButton("Logout");

        sidebar.add(profileButton);
        sidebar.add(doctorsButton);
        sidebar.add(bookButton);
        sidebar.add(appointmentsButton);
        sidebar.add(logoutButton);

        mainPanel.add(sidebar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new GridBagLayout());

        JLabel dashboardLabel = new JLabel("Patient Dashboard");
        dashboardLabel.setFont(new Font("Arial", Font.BOLD, 30));

        contentPanel.add(dashboardLabel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        profileButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Patient profile will be added soon."));

        doctorsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Doctor search will be added soon."));

        bookButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Appointment booking will be added soon."));

        appointmentsButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Your appointments will be shown here."));

        logoutButton.addActionListener(e -> logout());

        add(mainPanel);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
                "Logout", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}