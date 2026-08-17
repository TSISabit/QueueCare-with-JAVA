package ui;

import model.Doctor;
import service.DoctorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorListFrame extends JFrame {
    private DoctorService doctorService;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private String patientId;

    public DoctorListFrame(String patientId) {
        this.patientId = patientId;
        doctorService = new DoctorService();

        setTitle("QueueCare - Find Doctor");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadDoctors();

        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Available Doctors");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Doctor ID",
                "Name",
                "Email"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setRowHeight(25);

        mainPanel.add(
                new JScrollPane(doctorTable),
                BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton bookButton = new JButton("Book Appointment");

        buttonPanel.add(refreshButton);
        buttonPanel.add(bookButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(
                e -> loadDoctors());

        bookButton.addActionListener(
                e -> openBooking());

        add(mainPanel);
    }

    private void loadDoctors() {
        tableModel.setRowCount(0);

        List<Doctor> doctors = doctorService.getApprovedDoctors();

        for (Doctor doctor : doctors) {
            Object[] row = {
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getEmail()
            };

            tableModel.addRow(row);
        }
    }

    private void openBooking() {
        int selectedRow = doctorTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a doctor first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doctorId = tableModel.getValueAt(selectedRow, 0).toString();

        new BookAppointmentFrame(patientId, doctorId);
    }
}