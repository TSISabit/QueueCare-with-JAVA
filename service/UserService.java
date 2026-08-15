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

        String data =
                user.getId() + "," +
                user.getName() + "," +
                user.getEmail() + "," +
                user.getPassword() + "," +
                user.getRole() +
                "\n";

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
}