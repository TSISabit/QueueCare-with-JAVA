package service;

import model.Doctor;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {

    private static final String FILE_NAME = "data/doctors.csv";

    // Add a new doctor
    public boolean addDoctor(Doctor doctor) {

        // Check if ID already exists
        if (findDoctorById(doctor.getId()) != null) {
            return false;
        }

        String data = doctor.getId() + "," +
                doctor.getName() + "," +
                doctor.getEmail() + "," +
                doctor.getPassword() + "," +
                doctor.getPhone() + "," +
                doctor.getSpecialization() + "," +
                doctor.getConsultationFee() +
                "\n";

        FileManager.writeToFile(FILE_NAME, data);

        return true;
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

            if (parts.length != 7) {
                continue;
            }

            Doctor doctor = new Doctor(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    Double.parseDouble(parts[6]));

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

            if (doctor.getSpecialization()
                    .equalsIgnoreCase(specialization)) {

                result.add(doctor);
            }
        }

        return result;
    }

    // Update doctor information
    public boolean updateDoctor(
            String id,
            String name,
            String email,
            String phone,
            String specialization,
            double consultationFee) {

        List<Doctor> doctors = getAllDoctors();

        boolean found = false;

        for (Doctor doctor : doctors) {

            if (doctor.getId().equalsIgnoreCase(id)) {

                doctor.setName(name);
                doctor.setEmail(email);
                doctor.setPhone(phone);
                doctor.setSpecialization(specialization);
                doctor.setConsultationFee(consultationFee);

                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        saveAllDoctors(doctors);

        return true;
    }

    // Delete doctor
    public boolean deleteDoctor(String id) {

        List<Doctor> doctors = getAllDoctors();

        boolean removed = false;

        for (int i = 0; i < doctors.size(); i++) {

            if (doctors.get(i)
                    .getId()
                    .equalsIgnoreCase(id)) {

                doctors.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            return false;
        }

        saveAllDoctors(doctors);

        return true;
    }

    public List<Doctor> getApprovedDoctors() {
        List<Doctor> approvedDoctors = new ArrayList<>();
        DoctorApprovalService approvalService = new DoctorApprovalService();

        for (Doctor doctor : getAllDoctors()) {
            if (approvalService.isApproved(doctor.getId())) {
                approvedDoctors.add(doctor);
            }
        }

        return approvedDoctors;
    }

    // Save complete doctor list
    private void saveAllDoctors(List<Doctor> doctors) {

        StringBuilder data = new StringBuilder();

        for (Doctor doctor : doctors) {

            data.append(
                    doctor.getId()).append(",")
                    .append(
                            doctor.getName())
                    .append(",")
                    .append(
                            doctor.getEmail())
                    .append(",")
                    .append(
                            doctor.getPassword())
                    .append(",")
                    .append(
                            doctor.getPhone())
                    .append(",")
                    .append(
                            doctor.getSpecialization())
                    .append(",")
                    .append(
                            doctor.getConsultationFee())
                    .append("\n");
        }

        FileManager.writeToFile(
                FILE_NAME,
                data.toString(),
                false);
    }
}