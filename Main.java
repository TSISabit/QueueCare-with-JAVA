import model.User;
import service.UserService;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserService();

        // REGISTER ADMIN

        User admin = new User("U001",
                        "QueueCare Admin",
                        "admin@queuecare.com",
                        "admin123",
                        "ADMIN"
                );

        if (userService.registerUser(admin)) {

            System.out.println("Admin registered successfully.");

        } else {

            System.out.println("Admin already exists.");
        }

        // REGISTER PATIENT

        User patient = new User(
                        "U002",
                        "Sabit",
                        "sabit@queuecare.com",
                        "1234",
                        "PATIENT"
                );


        if (userService.registerUser(patient)) {

            System.out.println(
                    "Patient registered successfully."
            );

        } else {

            System.out.println("Patient already exists.");
        }

        // LOGIN TEST

        System.out.println("\n===== LOGIN TEST =====");

        User loggedIn = userService.login(
                        "admin@queuecare.com",
                        "admin123");

        if (loggedIn != null) {

            System.out.println("Login successful!");

            System.out.println("Name: " + loggedIn.getName());

            System.out.println("Role: " + loggedIn.getRole());

        } else {

            System.out.println("Invalid email or password.");
        }

        // WRONG PASSWORD TEST

        System.out.println("\n===== WRONG PASSWORD TEST =====");

        User failedLogin = userService.login("admin@queuecare.com","wrongpassword");

        if (failedLogin != null) {

            System.out.println("Login successful!");

        } else {

            System.out.println("Invalid email or password.");
        }
    }
}