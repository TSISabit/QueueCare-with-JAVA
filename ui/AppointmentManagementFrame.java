package ui;

import model.Appointment;
import service.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentManagementFrame extends JFrame {
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentManagementFrame() {
        appointmentService = new AppointmentService();

        setTitle("QueueCare - Manage Appointments");
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

        JLabel titleLabel = new JLabel("Manage Appointments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Appointment ID",
                "Patient ID",
                "Doctor ID",
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

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

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

        List<Appointment> appointments = appointmentService.getAllAppointments();

        for (Appointment appointment : appointments) {
            Object[] row = {
                    appointment.getAppointmentId(),
                    appointment.getPatientId(),
                    appointment.getDoctorId(),
                    appointment.getDate(),
                    appointment.getTime(),
                    appointment.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    private void updateStatus(String status) {
        int selectedRow = appointmentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String appointmentId = tableModel.getValueAt(selectedRow, 0).toString();

        boolean updated = appointmentService.updateStatus(appointmentId, status);

        if (updated) {
            JOptionPane.showMessageDialog(this, "Appointment status changed to " + status + ".");
            loadAppointments();
        } 
        else {
            JOptionPane.showMessageDialog(this, "Could not update appointment.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}