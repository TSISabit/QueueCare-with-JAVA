package ui;

import model.Appointment;
import service.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorAppointmentsFrame extends JFrame {
    private String doctorId;
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public DoctorAppointmentsFrame(String doctorId) {
        this.doctorId = doctorId;
        appointmentService = new AppointmentService();

        setTitle("QueueCare - My Appointments");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadAppointments();

        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("My Appointments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Appointment ID",
                "Patient ID",
                "Date",
                "Time",
                "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        appointmentTable = new JTable(tableModel);
        appointmentTable.setRowHeight(25);

        mainPanel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton confirmButton = new JButton("Confirm");
        JButton completeButton = new JButton("Complete");
        JButton cancelButton = new JButton("Cancel");
        JButton refreshButton = new JButton("Refresh");

        buttonPanel.add(confirmButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(e -> updateStatus("CONFIRMED"));
        completeButton.addActionListener(e -> updateStatus("COMPLETED"));
        cancelButton.addActionListener(e -> updateStatus("CANCELLED"));
        refreshButton.addActionListener(e -> loadAppointments());

        add(mainPanel);
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);

        List<Appointment> appointments =
                appointmentService.getAppointmentsByDoctor(doctorId);

        for (Appointment appointment : appointments) {
            Object[] row = {
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    appointment.getDate(),
                    appointment.getTime(),
                    appointment.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    private void updateStatus(String newStatus) {
        int selectedRow = appointmentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an appointment first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String currentStatus =
                tableModel.getValueAt(selectedRow, 4).toString();

        if (currentStatus.equalsIgnoreCase("CANCELLED") ||
                currentStatus.equalsIgnoreCase("COMPLETED")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This appointment is already " + currentStatus + "."
            );
            return;
        }

        String appointmentId =
                tableModel.getValueAt(selectedRow, 0).toString();

        boolean updated =
                appointmentService.updateStatus(
                        appointmentId,
                        newStatus
                );

        if (updated) {
            JOptionPane.showMessageDialog(
                    this,
                    "Appointment status changed to " + newStatus + "."
            );

            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not update appointment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}