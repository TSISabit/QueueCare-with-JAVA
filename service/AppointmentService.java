package service;

import model.Appointment;
import util.FileManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private static final String FILE_NAME = "data/appointments.csv";

    private DoctorService doctorService;
    private PatientService patientService;


    public AppointmentService() {

        doctorService = new DoctorService();
        patientService = new PatientService();
    }

    // BOOK APPOINTMENT

    public boolean bookAppointment(Appointment appointment) {
        // Check patient
        if (patientService.findPatientById(appointment.getPatientId()) == null) {
            System.out.println("Patient does not exist.");
            return false;
        }

        // Check doctor
        if (doctorService.findDoctorById(appointment.getDoctorId()) == null) {
            System.out.println("Doctor does not exist.");
            return false;
        }


        // Check duplicate time slot
        if (isSlotAlreadyBooked(appointment.getDoctorId(), appointment.getDate(), appointment.getTime())) {
            System.out.println("This time slot is already booked.");
            return false;
        }


        // Save appointment
        String data =
                appointment.getAppointmentId() + "," +
                appointment.getPatientId() + "," +
                appointment.getDoctorId() + "," +
                appointment.getDate() + "," +
                appointment.getTime() + "," +
                appointment.getStatus() +
                "\n";


        FileManager.writeToFile(FILE_NAME, data);
        return true;
    }

    // CHECK DUPLICATE SLOT

    private boolean isSlotAlreadyBooked(String doctorId, LocalDate date, LocalTime time) {
        List<Appointment> appointments = getAllAppointments();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId) &&
                    appointment.getDate().equals(date) &&
                    appointment.getTime().equals(time) &&
                    !appointment.getStatus().equalsIgnoreCase("CANCELLED")) {
                return true;
            }
        }

        return false;
    }

    // GET ALL APPOINTMENTS

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length != 6) {
                continue;
            }


            Appointment appointment =
                    new Appointment(
                            parts[0],
                            parts[1],
                            parts[2],
                            LocalDate.parse(parts[3]),
                            LocalTime.parse(parts[4])
                    );

            appointment.setStatus(parts[5]);

            appointments.add(appointment);
        }

        return appointments;
    }

    // FIND APPOINTMENT BY ID

    public Appointment findAppointmentById(String appointmentId) {
        List<Appointment> appointments = getAllAppointments();

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return appointment;
            }
        }

        return null;
    }

    // GET PATIENT APPOINTMENTS

    public List<Appointment> getAppointmentsByPatient(String patientId) {

        List<Appointment> result = new ArrayList<>();

        List<Appointment> appointments = getAllAppointments();

        for (Appointment appointment : appointments) {

            if (appointment.getPatientId().equalsIgnoreCase(patientId)) {
                result.add(appointment);
            }
        }

        return result;
    }

    // GET DOCTOR APPOINTMENTS

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {

        List<Appointment> result = new ArrayList<>();

        List<Appointment> appointments = getAllAppointments();

        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equalsIgnoreCase(doctorId)) {
                result.add(appointment);
            }
        }

        return result;
    }

    // CANCEL APPOINTMENT

    public boolean cancelAppointment(String appointmentId) {

        List<Appointment> appointments = getAllAppointments();

        boolean found = false;

        for (Appointment appointment : appointments) {

            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointment.setStatus("CANCELLED");
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        saveAllAppointments(appointments);
        return true;
    }

    // UPDATE APPOINTMENT STATUS

    public boolean updateStatus(String appointmentId,String status) {

        List<Appointment> appointments = getAllAppointments();

        boolean found = false;


        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointment.setStatus(status);

                found = true;

                break;
            }
        }

        if (!found) {
            return false;
        }

        saveAllAppointments(appointments);

        return true;
    }

    // SAVE ALL APPOINTMENTS

    private void saveAllAppointments(List<Appointment> appointments) {

        StringBuilder data = new StringBuilder();


        for (Appointment appointment : appointments) {

            data.append(appointment.getAppointmentId()
            ).append(",")

             .append(
                    appointment.getPatientId()
            ).append(",")

             .append(
                    appointment.getDoctorId()
            ).append(",")

             .append(
                    appointment.getDate()
            ).append(",")

             .append(
                    appointment.getTime()
            ).append(",")

             .append(
                    appointment.getStatus()
            ).append("\n");
        }

        FileManager.writeToFile(FILE_NAME, data.toString(), false);
    }
}