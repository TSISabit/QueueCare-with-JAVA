package ui;

import model.Appointment;
import service.AppointmentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientAppointmentsFrame extends JFrame {
    private String patientId;
    private AppointmentService appointmentService;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public PatientAppointmentsFrame(String patientId) {
        this.patientId = patientId;
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

        mainPanel.add(new JScrollPane(appointmentTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        JButton cancelButton = new JButton("Cancel Appointment");

        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> loadAppointments());
        cancelButton.addActionListener(e -> cancelAppointment());

        add(mainPanel);
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);

        List<Appointment> appointments =
                appointmentService.getAppointmentsByPatient(patientId);

        for (Appointment appointment : appointments) {
            Object[] row = {
                    appointment.getAppointmentId(),
                    appointment.getDoctorId(),
                    appointment.getDate(),
                    appointment.getTime(),
                    appointment.getStatus()
            };

            tableModel.addRow(row);
        }
    }

    private void cancelAppointment() {
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

        String status =
                tableModel.getValueAt(selectedRow, 4).toString();

        if (status.equalsIgnoreCase("CANCELLED")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This appointment is already cancelled."
            );
            return;
        }

        if (status.equalsIgnoreCase("COMPLETED")) {
            JOptionPane.showMessageDialog(
                    this,
                    "A completed appointment cannot be cancelled."
            );
            return;
        }

        String appointmentId =
                tableModel.getValueAt(selectedRow, 0).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this appointment?",
                "Cancel Appointment",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean cancelled =
                appointmentService.cancelAppointment(appointmentId);

        if (cancelled) {
            JOptionPane.showMessageDialog(
                    this,
                    "Appointment cancelled successfully."
            );

            loadAppointments();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not cancel appointment.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}