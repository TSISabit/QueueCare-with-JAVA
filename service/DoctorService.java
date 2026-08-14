package service;

import model.Doctor;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    private static final String FILE_NAME = "data/doctors.csv";

    // Add a new doctor
    public void addDoctor(Doctor doctor) {

        String data =
                doctor.getId() + "," +
                doctor.getName() + "," +
                doctor.getEmail() + "," +
                doctor.getPassword() + "," +
                doctor.getPhone() + "," +
                doctor.getSpecialization() + "," +
                doctor.getConsultationFee() +
                "\n";

        FileManager.writeToFile(FILE_NAME, data);
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            Doctor doctor = new Doctor(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    Double.parseDouble(parts[6])
            );

            doctors.add(doctor);
        }

        return doctors;
    }

    // Find doctor by ID
    public Doctor findDoctorById(String id) {
        List<Doctor> doctors = getAllDoctors();

        for (Doctor doctor : doctors) {

            if (doctor.getId().equalsIgnoreCase(id)) {
                return doctor;
            }
        }

        return null;
    }

    // Find doctors by specialization
    public List<Doctor> findBySpecialization(String specialization) {
        List<Doctor> result = new ArrayList<>();

        List<Doctor> doctors = getAllDoctors();

        for (Doctor doctor : doctors) {
            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                result.add(doctor);
            }
        }

        return result;
    }
}