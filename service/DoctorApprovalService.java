package service;

import model.User;
import util.FileManager;

import java.util.List;

public class DoctorApprovalService {
    private static final String FILE_NAME = "data/doctor_status.csv";

    public void createPendingRequest(User doctor) {
        String data = doctor.getId() + "," +
                doctor.getName() + "," +
                doctor.getEmail() + ",PENDING\n";

        FileManager.writeToFile(FILE_NAME, data);
    }

    public String getStatus(String doctorId) {
        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length != 4) {
                continue;
            }

            if (parts[0].equalsIgnoreCase(doctorId)) {
                return parts[3];
            }
        }

        return "PENDING";
    }

    public boolean isApproved(String doctorId) {
        return getStatus(doctorId).equalsIgnoreCase("APPROVED");
    }
}