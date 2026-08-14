import model.Doctor;
import model.Patient;

public class Main {

    public static void main(String[] args) {
        Doctor doctor = new Doctor("D001", "Dr. Rahman", "rahman@queuecare.com", "1234", "01700000000", "Cardiologist", 1000);

        Patient patient = new Patient("P001", "Sabit", "sabit@gmail.com", "1234", 22, "Male", "01800000000", "Dhaka");

        System.out.println("===== QueueCare =====");

        System.out.println("\nDoctor Information:");
        System.out.println("Name: " + doctor.getName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        System.out.println("Fee: " + doctor.getConsultationFee());

        System.out.println("\nPatient Information:");
        System.out.println("Name: " + patient.getName());
        System.out.println("Age: " + patient.getAge());
        System.out.println("Phone: " + patient.getPhone());
    }
}