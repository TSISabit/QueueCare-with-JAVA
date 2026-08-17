package service;

import model.Patient;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class PatientService {

    private static final String FILE_NAME = "data/patients.csv";

    // Add a new patient
    public boolean addPatient(Patient patient) {

        // Prevent duplicate patient ID
        if (findPatientById(patient.getId()) != null) {
            return false;
        }

        String data =
                patient.getId() + "," +
                patient.getName() + "," +
                patient.getEmail() + "," +
                patient.getPassword() + "," +
                patient.getAge() + "," +
                patient.getGender() + "," +
                patient.getPhone() + "," +
                patient.getAddress() +
                "\n";

        FileManager.writeToFile(FILE_NAME, data);

        return true;
    }

    // Get all patients
    public List<Patient> getAllPatients() {

        List<Patient> patients = new ArrayList<>();

        List<String> data =
                FileManager.readFromFile(FILE_NAME);

        for (String line : data) {

            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 8);

            if (parts.length != 8) {
                continue;
            }

            Patient patient = new Patient(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    Integer.parseInt(parts[4]),
                    parts[5],
                    parts[6],
                    parts[7]
            );

            patients.add(patient);
        }

        return patients;
    }

    // Find patient by ID
    public Patient findPatientById(String id) {

        List<Patient> patients =
                getAllPatients();

        for (Patient patient : patients) {

            if (patient.getId()
                    .equalsIgnoreCase(id)) {

                return patient;
            }
        }

        return null;
    }

    // Search patients by name
    public List<Patient> searchPatient(String name) {

        List<Patient> result =
                new ArrayList<>();

        List<Patient> patients =
                getAllPatients();

        for (Patient patient : patients) {

            if (patient.getName()
                    .toLowerCase()
                    .contains(name.toLowerCase())) {

                result.add(patient);
            }
        }

        return result;
    }

    // Update patient
    public boolean updatePatient(
            String id,
            String name,
            String email,
            int age,
            String gender,
            String phone,
            String address) {

        List<Patient> patients =
                getAllPatients();

        boolean found = false;

        for (Patient patient : patients) {

            if (patient.getId()
                    .equalsIgnoreCase(id)) {

                patient.setName(name);
                patient.setEmail(email);
                patient.setAge(age);
                patient.setGender(gender);
                patient.setPhone(phone);
                patient.setAddress(address);

                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        saveAllPatients(patients);

        return true;
    }

    // Delete patient
    public boolean deletePatient(String id) {

        List<Patient> patients =
                getAllPatients();

        boolean removed = false;

        for (int i = 0; i < patients.size(); i++) {

            if (patients.get(i)
                    .getId()
                    .equalsIgnoreCase(id)) {

                patients.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            return false;
        }

        saveAllPatients(patients);

        return true;
    }

    // Save complete patient list
    private void saveAllPatients(
            List<Patient> patients) {

        StringBuilder data =
                new StringBuilder();

        for (Patient patient : patients) {

            data.append(
                    patient.getId()
            ).append(",")
             .append(
                    patient.getName()
            ).append(",")
             .append(
                    patient.getEmail()
            ).append(",")
             .append(
                    patient.getPassword()
            ).append(",")
             .append(
                    patient.getAge()
            ).append(",")
             .append(
                    patient.getGender()
            ).append(",")
             .append(
                    patient.getPhone()
            ).append(",")
             .append(
                    patient.getAddress()
            ).append("\n");
        }

        // false = overwrite existing file
        FileManager.writeToFile(
                FILE_NAME,
                data.toString(),
                false
        );
    }
}