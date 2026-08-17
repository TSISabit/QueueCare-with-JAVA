package service;

import model.User;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class DoctorApprovalService {
    private static final String FILE_NAME = "data/doctor_status.csv";

    public void createPendingRequest(User doctor) {
        if (getStatus(doctor.getId()).equals("NOT_FOUND")) {
            String data = doctor.getId() + "," +
                    doctor.getName() + "," +
                    doctor.getEmail() + ",PENDING\n";

            FileManager.writeToFile(FILE_NAME, data);
        }
    }

    public List<String[]> getAllRequests() {
        List<String[]> requests = new ArrayList<>();
        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length == 4) {
                requests.add(parts);
            }
        }

        return requests;
    }

    public String getStatus(String doctorId) {
        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length == 4 &&
                    parts[0].trim().equalsIgnoreCase(doctorId.trim())) {
                return parts[3].trim();
            }
        }

        return "NOT_FOUND";
    }

    public boolean updateStatus(String doctorId, String newStatus) {
        List<String> data = FileManager.readFromFile(FILE_NAME);
        List<String> updatedData = new ArrayList<>();

        boolean found = false;

        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length == 4 &&
                    parts[0].trim().equalsIgnoreCase(doctorId.trim())) {

                if (!found) {
                    updatedData.add(
                            parts[0].trim() + "," +
                            parts[1].trim() + "," +
                            parts[2].trim() + "," +
                            newStatus
                    );

                    found = true;
                }

                // Duplicate records are ignored.
            } else {
                updatedData.add(line);
            }
        }

        if (!found) {
            return false;
        }

        String content = String.join("\n", updatedData) + "\n";

        FileManager.writeToFile(FILE_NAME, content, false);

        return true;
    }

    public boolean isApproved(String doctorId) {
        return getStatus(doctorId).equalsIgnoreCase("APPROVED");
    }
}