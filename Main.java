import model.Appointment;
import service.AppointmentService;
import service.DoctorService;
import service.PatientService;

import java.time.LocalDate;
import java.time.LocalTime;

public class Main {

    public static void main(String[] args) {

        DoctorService doctorService =
                new DoctorService();

        PatientService patientService =
                new PatientService();

        AppointmentService appointmentService =
                new AppointmentService();

        // MAKE SURE DOCTOR EXISTS

        if (doctorService.findDoctorById("D001")
                == null) {

            doctorService.addDoctor(
                    new model.Doctor(
                            "D001",
                            "Dr. Rahman",
                            "rahman@queuecare.com",
                            "1234",
                            "01700000001",
                            "Cardiologist",
                            1000
                    )
            );
        }

        // MAKE SURE PATIENT EXISTS

        if (patientService.findPatientById("P001")
                == null) {

            patientService.addPatient(
                    new model.Patient(
                            "P001",
                            "Sabit",
                            "sabit@gmail.com",
                            "1234",
                            22,
                            "Male",
                            "01800000001",
                            "Dhaka"
                    )
            );
        }

        // BOOK APPOINTMENT

        Appointment appointment1 =
                new Appointment(
                        "A001",
                        "P001",
                        "D001",
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(10, 0)
                );


        boolean booked =
                appointmentService
                        .bookAppointment(
                                appointment1
                        );


        System.out.println(
                "Appointment booked: "
                + booked
        );

        // TRY SAME SLOT AGAIN

        Appointment appointment2 =
                new Appointment(
                        "A002",
                        "P001",
                        "D001",
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(10, 0)
                );


        boolean secondBooking =
                appointmentService
                        .bookAppointment(
                                appointment2
                        );


        System.out.println(
                "Second booking: "
                + secondBooking
        );

        // SHOW ALL APPOINTMENTS

        System.out.println(
                "\n===== ALL APPOINTMENTS ====="
        );


        for (Appointment appointment :
                appointmentService
                        .getAllAppointments()) {

            System.out.println(
                    appointment.getAppointmentId()
                    + " | Patient: "
                    + appointment.getPatientId()
                    + " | Doctor: "
                    + appointment.getDoctorId()
                    + " | Date: "
                    + appointment.getDate()
                    + " | Time: "
                    + appointment.getTime()
                    + " | Status: "
                    + appointment.getStatus()
            );
        }

        // PATIENT APPOINTMENTS

        System.out.println(
                "\n===== PATIENT P001 ====="
        );


        for (Appointment appointment :
                appointmentService
                        .getAppointmentsByPatient(
                                "P001")) {

            System.out.println(
                    appointment.getAppointmentId()
                    + " | "
                    + appointment.getDate()
                    + " | "
                    + appointment.getTime()
                    + " | "
                    + appointment.getStatus()
            );
        }

        // DOCTOR APPOINTMENTS

        System.out.println(
                "\n===== DOCTOR D001 ====="
        );


        for (Appointment appointment :
                appointmentService
                        .getAppointmentsByDoctor(
                                "D001")) {

            System.out.println(
                    appointment.getAppointmentId()
                    + " | Patient: "
                    + appointment.getPatientId()
                    + " | "
                    + appointment.getDate()
                    + " | "
                    + appointment.getTime()
            );
        }

        // UPDATE STATUS

        boolean updated =
                appointmentService.updateStatus(
                        "A001",
                        "CONFIRMED"
                );


        System.out.println(
                "\nStatus updated: "
                + updated
        );

        // CANCEL APPOINTMENT

        boolean cancelled =
                appointmentService
                        .cancelAppointment("A001");


        System.out.println(
                "Appointment cancelled: "
                + cancelled
        );
    }
}