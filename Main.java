import model.Doctor;
import service.DoctorService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        DoctorService doctorService = new DoctorService();

        // Add doctors
        Doctor doctor1 = new Doctor(
                "D001",
                "Dr. Rahman",
                "rahman@queuecare.com",
                "1234",
                "01700000001",
                "Cardiologist",
                1000
        );

        Doctor doctor2 = new Doctor(
                "D002",
                "Dr. Karim",
                "karim@queuecare.com",
                "1234",
                "01700000002",
                "Dermatologist",
                800
        );

        doctorService.addDoctor(doctor1);
        doctorService.addDoctor(doctor2);


        // Display all doctors
        System.out.println("===== ALL DOCTORS =====");

        List<Doctor> doctors = doctorService.getAllDoctors();

        for (Doctor doctor : doctors) {

            System.out.println(
                    doctor.getId() + " | " +
                    doctor.getName() + " | " +
                    doctor.getSpecialization() + " | " +
                    doctor.getConsultationFee()
            );
        }


        // Search doctor
        System.out.println("\n===== SEARCH DOCTOR =====");

        Doctor foundDoctor =
                doctorService.findDoctorById("D002");

        if (foundDoctor != null) {

            System.out.println(
                    "Doctor Found: " +
                    foundDoctor.getName()
            );

        } else {

            System.out.println("Doctor not found.");
        }


        // Search by specialization
        System.out.println("\n===== CARDIOLOGISTS =====");

        List<Doctor> cardiologists =
                doctorService.findBySpecialization("Cardiologist");

        for (Doctor doctor : cardiologists) {

            System.out.println(
                    doctor.getName() +
                    " - " +
                    doctor.getSpecialization()
            );
        }
    }
}