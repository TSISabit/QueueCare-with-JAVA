import model.Patient;
import service.PatientService;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        PatientService patientService =
                new PatientService();

        // ADD PATIENT

        Patient patient1 = new Patient("P001", "Sabit", "sabit@gmail.com", "1234", 22,
                "Male",
                "01800000001",
                "Dhaka");

        Patient patient2 = new Patient("P002", "Rahim", "rahim@gmail.com", "1234",25,
                "Male",
                "01800000002",
                "Mirpur");


        boolean added1 = patientService.addPatient(patient1);

        boolean added2 =patientService.addPatient(patient2);


        System.out.println("Patient 1 added: " + added1);

        System.out.println("Patient 2 added: " + added2);

        // SHOW ALL PATIENTS

        System.out.println("\n===== ALL PATIENTS =====");

        List<Patient> patients =
                patientService.getAllPatients();

        for (Patient patient : patients) {

            System.out.println(
                    patient.getId() + " | " +
                    patient.getName() + " | " +
                    patient.getAge() + " | " +
                    patient.getGender() + " | " +
                    patient.getPhone()
            );
        }

        // FIND PATIENT BY ID

        System.out.println(
                "\n===== FIND PATIENT ====="
        );

        Patient found = patientService.findPatientById("P001");

        if (found != null) {
            System.out.println("Patient Found: " + found.getName());
        } else {
            System.out.println("Patient not found.");
        }

        // SEARCH PATIENT BY NAME

        System.out.println("\n===== SEARCH PATIENT =====");

        List<Patient> result = patientService.searchPatient("rah");

        for (Patient patient : result) {
            System.out.println(patient.getId() + " | " + patient.getName());
        }

        // UPDATE PATIENT

        boolean updated = patientService.updatePatient(
                        "P002",
                        "Md. Rahim",
                        "mdrahim@gmail.com",
                        26,
                        "Male",
                        "01900000002",
                        "Uttara"
                );

        System.out.println("\nUpdate Result: " + updated);

        // SHOW UPDATED PATIENT

        Patient updatedPatient = patientService.findPatientById("P002");

        if (updatedPatient != null) {
            System.out.println("\n===== UPDATED PATIENT =====");

            System.out.println("Name: " + updatedPatient.getName());

            System.out.println("Email: " + updatedPatient.getEmail());

            System.out.println("Age: " + updatedPatient.getAge());

            System.out.println("Phone: " + updatedPatient.getPhone());

            System.out.println("Address: " + updatedPatient.getAddress());
        }

        // DELETE PATIENT

        boolean deleted = patientService.deletePatient("P002");

        System.out.println("\nDelete Result: " + deleted);

        // CHECK DELETE

        Patient deletedPatient = patientService.findPatientById("P002");

        if (deletedPatient == null) {
            System.out.println("Patient P002 no longer exists.");
        }
    }
}