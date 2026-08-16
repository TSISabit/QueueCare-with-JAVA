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
                    parts[0].equalsIgnoreCase(doctorId)) {
                return parts[3];
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
                    parts[0].equalsIgnoreCase(doctorId)) {

                updatedData.add(
                        parts[0] + "," +
                        parts[1] + "," +
                        parts[2] + "," +
                        newStatus
                );

                found = true;
            } else {
                updatedData.add(line);
            }
        }

        if (found) {
            FileManager.writeToFile(
                    FILE_NAME,
                    String.join("\n", updatedData) + "\n"
            );
        }

        return found;
    }

    public boolean isApproved(String doctorId) {
        return getStatus(doctorId).equalsIgnoreCase("APPROVED");
    }
}