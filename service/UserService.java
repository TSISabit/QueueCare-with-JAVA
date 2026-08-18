package service;

import model.User;
import util.FileManager;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String FILE_NAME = "data/users.csv";

    // REGISTER USER
    public boolean registerUser(User user) {
        if (findUserById(user.getId()) != null) {
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

    // GET ALL USERS
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

    // FIND USER BY ID
    public User findUserById(String id) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                return user;
            }
        }

        return null;
    }

    // FIND USER BY EMAIL
    public User findUserByEmail(String email) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return user;
            }
        }

        return null;
    }

    // GENERATE ID
    public String generateId(String role) {
        List<User> users = getAllUsers();
        int maxNumber = 0;

        String prefix = role.equalsIgnoreCase("DOCTOR") ? "D" :
                role.equalsIgnoreCase("PATIENT") ? "P" : "U";

        for (User user : users) {
            String id = user.getId();

            if (id.startsWith(prefix)) {
                try {
                    int number = Integer.parseInt(id.substring(1));

                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return String.format("%s%03d", prefix, maxNumber + 1);
    }

    // LOGIN
    public User login(String email, String password) {
        List<User> users = getAllUsers();

        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email) &&
                    user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    // UPDATE USER
    public boolean updateUser(
            String id,
            String name,
            String email) {

        List<User> users = getAllUsers();
        boolean found = false;

        for (User user : users) {
            if (user.getId().equalsIgnoreCase(id)) {
                user.setName(name);
                user.setEmail(email);
                found = true;
                break;
            }
        }

        if (!found) {
            return false;
        }

        saveAllUsers(users);
        return true;
    }

    // DELETE USER
    public boolean deleteUser(String id) {
        List<User> users = getAllUsers();
        boolean removed = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equalsIgnoreCase(id)) {
                users.remove(i);
                removed = true;
                break;
            }
        }

        if (!removed) {
            return false;
        }

        saveAllUsers(users);
        return true;
    }

    // SAVE ALL USERS
    private void saveAllUsers(List<User> users) {
        StringBuilder data = new StringBuilder();

        for (User user : users) {
            data.append(user.getId()).append(",")
                    .append(user.getName()).append(",")
                    .append(user.getEmail()).append(",")
                    .append(user.getPassword()).append(",")
                    .append(user.getRole()).append("\n");
        }

        FileManager.writeToFile(
                FILE_NAME,
                data.toString(),
                false
        );
    }
}