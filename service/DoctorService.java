package service;

import model.Doctor;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    private static final String FILE_NAME = "data/doctors.csv";
    private final UserService userService;

    public DoctorService() {
        userService = new UserService();
    }

    public boolean addDoctor(Doctor doctor) {
        if (findDoctorById(doctor.getId()) != null) {
            return false;
        }

        if (userService.findUserById(doctor.getId()) != null) {
            return false;
        }

        String data =
                doctor.getId() + "," +
                doctor.getName() + "," +
                doctor.getEmail() + "," +
                doctor.getPassword() + "," +
                doctor.getPhone() + "," +
                doctor.getSpecialization() + "," +
                doctor.getConsultationFee() + "\n";

        FileManager.writeToFile(FILE_NAME, data);

        if (!userService.registerUser(doctor)) {
            deleteDoctor(doctor.getId());
            return false;
        }

        return true;
    }

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

            try {
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
            } catch (NumberFormatException e) {
                continue;
            }
        }

        return doctors;
    }

    public Doctor findDoctorById(String id) {
        for (Doctor doctor : getAllDoctors()) {
            if (doctor.getId().equalsIgnoreCase(id)) {
                return doctor;
            }
        }

        return null;
    }

    public List<Doctor> findBySpecialization(String specialization) {
        List<Doctor> result = new ArrayList<>();

        for (Doctor doctor : getAllDoctors()) {
            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                result.add(doctor);
            }
        }

        return result;
    }

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

        if (!userService.updateUser(id, name, email)) {
            return false;
        }

        return true;
    }

    public boolean deleteDoctor(String id) {
        List<Doctor> doctors = getAllDoctors();
        boolean removed = false;

        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equalsIgnoreCase(id)) {
                doctors.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            return false;
        }

        saveAllDoctors(doctors);

        userService.deleteUser(id);

        return true;
    }

    private void saveAllDoctors(List<Doctor> doctors) {
        StringBuilder data = new StringBuilder();

        for (Doctor doctor : doctors) {
            data.append(doctor.getId()).append(",")
                    .append(doctor.getName()).append(",")
                    .append(doctor.getEmail()).append(",")
                    .append(doctor.getPassword()).append(",")
                    .append(doctor.getPhone()).append(",")
                    .append(doctor.getSpecialization()).append(",")
                    .append(doctor.getConsultationFee()).append("\n");
        }

        FileManager.writeToFile(FILE_NAME, data.toString(), false);
    }
}