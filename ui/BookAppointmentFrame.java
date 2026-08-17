package ui;

import model.Appointment;
import service.AppointmentService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class BookAppointmentFrame extends JFrame {
    private String doctorId;
    private String patientId;
    private JTextField dateField;
    private JTextField timeField;
    private AppointmentService appointmentService;

    public BookAppointmentFrame(String patientId, String doctorId) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        appointmentService = new AppointmentService();

        setTitle("QueueCare - Book Appointment");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        formPanel.add(new JLabel("Doctor ID:"));
        formPanel.add(new JLabel(doctorId));

        formPanel.add(new JLabel("Date:"));
        dateField = new JTextField();
        dateField.setToolTipText("Format: YYYY-MM-DD");
        formPanel.add(dateField);

        formPanel.add(new JLabel("Time:"));
        timeField = new JTextField();
        timeField.setToolTipText("Format: HH:MM");
        formPanel.add(timeField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(new JLabel("PENDING"));

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton bookButton = new JButton("Confirm Booking");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        bookButton.addActionListener(e -> bookAppointment());
        cancelButton.addActionListener(e -> dispose());

        add(mainPanel);
    }

    private void bookAppointment() {
        String dateText = dateField.getText().trim();
        String timeText = timeField.getText().trim();

        if (dateText.isEmpty() || timeText.isEmpty()) {
            showError("Please enter date and time.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateText);
            LocalTime time = LocalTime.parse(timeText);

            if (date.isBefore(LocalDate.now())) {
                showError("Appointment date cannot be in the past.");
                return;
            }

            String appointmentId = generateAppointmentId();

            Appointment appointment = new Appointment(
                    appointmentId,
                    patientId,
                    doctorId,
                    date,
                    time
            );

            if (appointmentService.bookAppointment(appointment)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Appointment booked successfully!\n" +
                        "Appointment ID: " + appointmentId +
                        "\nStatus: PENDING"
                );

                dispose();
            } else {
                showError(
                        "This time slot is already booked or " +
                        "the doctor/patient does not exist."
                );
            }
        } catch (DateTimeParseException e) {
            showError(
                    "Invalid date or time format.\n\n" +
                    "Date: YYYY-MM-DD\n" +
                    "Time: HH:MM"
            );
        }
    }

    private String generateAppointmentId() {
        int number = appointmentService.getAllAppointments().size() + 1;
        return String.format("A%03d", number);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Booking Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}