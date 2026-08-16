package service;

import model.User;
import util.FileManager;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String FILE_NAME = "data/users.csv";

    public boolean registerUser(User user) {
        if (findUserById(user.getId()) != null || findUserByEmail(user.getEmail()) != null) {
            return false;
        }

        String data = user.getId() + "," +
                user.getName() + "," +
                user.getEmail() + "," +
                user.getPassword() + "," +
                user.getRole() + "\n";

        FileManager.writeToFile(FILE_NAME, data);
        return true;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        List<String> data = FileManager.readFromFile(FILE_NAME);

        for (String line : data) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",");

            if (parts.length != 5) {
                continue;
            }

            User user = new User(
                    parts[0],
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4]
            );

            users.add(user);
        }

        return users;
    }

    public User findUserById(String id) {
        for (User user : getAllUsers()) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }

        return null;
    }

    public User findUserByEmail(String email) {
        for (User user : getAllUsers()) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    public User login(String email, String password) {
        for (User user : getAllUsers()) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public String generateId(String role) {
        int count = 0;

        for (User user : getAllUsers()) {
            if (user.getRole().equalsIgnoreCase(role)) {
                count++;
            }
        }

        if (role.equalsIgnoreCase("PATIENT")) {
            return String.format("P%03d", count + 1);
        }

        if (role.equalsIgnoreCase("DOCTOR")) {
            return String.format("D%03d", count + 1);
        }

        return String.format("U%03d", count + 1);
    }
}