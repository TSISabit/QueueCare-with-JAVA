package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DoctorDashboard extends JFrame {
        private User loggedInUser;

        public DoctorDashboard(User user) {
                loggedInUser = user;

                setTitle("QueueCare - Doctor Dashboard");
                setSize(800, 500);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);

                createUI();
                setVisible(true);
        }

        private void createUI() {
                JPanel mainPanel = new JPanel(new BorderLayout());

                JPanel headerPanel = new JPanel(new BorderLayout());
                headerPanel.setBorder(
                                BorderFactory.createEmptyBorder(15, 20, 15, 20));

                JLabel titleLabel = new JLabel("QueueCare");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

                JLabel welcomeLabel = new JLabel(
                                "Welcome, " + loggedInUser.getName());

                headerPanel.add(titleLabel, BorderLayout.WEST);
                headerPanel.add(welcomeLabel, BorderLayout.EAST);

                mainPanel.add(headerPanel, BorderLayout.NORTH);

                JPanel sidebar = new JPanel(
                                new GridLayout(4, 1, 10, 10));

                sidebar.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

                JButton profileButton = new JButton("My Profile");
                JButton appointmentsButton = new JButton("My Appointments");
                JButton deleteButton = new JButton("Delete Profile");
                JButton logoutButton = new JButton("Logout");

                sidebar.add(profileButton);
                sidebar.add(appointmentsButton);
                sidebar.add(deleteButton);
                sidebar.add(logoutButton);

                mainPanel.add(sidebar, BorderLayout.WEST);

                JPanel contentPanel = new JPanel(new GridBagLayout());

                JLabel dashboardLabel = new JLabel("Doctor Dashboard");
                dashboardLabel.setFont(new Font("Arial", Font.BOLD, 30));

                contentPanel.add(dashboardLabel);
                mainPanel.add(contentPanel, BorderLayout.CENTER);

                profileButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                                "Doctor profile will be added soon."));

                appointmentsButton.addActionListener(
                                e -> new DoctorAppointmentsFrame(loggedInUser.getId()));

                deleteButton.addActionListener(
                                e -> JOptionPane.showMessageDialog(
                                                this,
                                                "Profile deletion request will be added soon."));

                logoutButton.addActionListener(e -> logout());

                add(mainPanel);
        }

        private void logout() {
                int choice = JOptionPane.showConfirmDialog(
                                this,
                                "Are you sure you want to logout?",
                                "Logout",
                                JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                        dispose();
                        new LoginFrame();
                }
        }
}