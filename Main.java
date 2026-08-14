import model.Doctor;
import service.DoctorService;

public class Main {

    public static void main(String[] args) {

        DoctorService doctorService =
                new DoctorService();


        // =================================
        // ADD DOCTOR
        // =================================

        Doctor doctor = new Doctor(
                "D003",
                "Dr. Ahmed",
                "ahmed@queuecare.com",
                "1234",
                "01700000003",
                "Neurologist",
                1200
        );

        boolean added =
                doctorService.addDoctor(doctor);

        if (added) {
            System.out.println(
                    "Doctor added successfully."
            );
        } else {
            System.out.println(
                    "Doctor ID already exists."
            );
        }


        // =================================
        // SHOW ALL DOCTORS
        // =================================

        System.out.println(
                "\n===== ALL DOCTORS ====="
        );

        for (Doctor d :
                doctorService.getAllDoctors()) {

            System.out.println(
                    d.getId() + " | " +
                    d.getName() + " | " +
                    d.getSpecialization() + " | " +
                    d.getConsultationFee()
            );
        }


        // =================================
        // UPDATE DOCTOR
        // =================================

        boolean updated =
                doctorService.updateDoctor(
                        "D003",
                        "Dr. Ahmed Khan",
                        "ahmedkhan@queuecare.com",
                        "01711111111",
                        "Neurosurgeon",
                        1500
                );

        System.out.println(
                "\nUpdate Result: " + updated
        );


        // =================================
        // SHOW UPDATED DOCTOR
        // =================================

        Doctor updatedDoctor =
                doctorService.findDoctorById("D003");

        if (updatedDoctor != null) {

            System.out.println(
                    "\n===== UPDATED DOCTOR ====="
            );

            System.out.println(
                    "Name: "
                    + updatedDoctor.getName()
            );

            System.out.println(
                    "Email: "
                    + updatedDoctor.getEmail()
            );

            System.out.println(
                    "Phone: "
                    + updatedDoctor.getPhone()
            );

            System.out.println(
                    "Specialization: "
                    + updatedDoctor.getSpecialization()
            );

            System.out.println(
                    "Fee: "
                    + updatedDoctor.getConsultationFee()
            );
        }


        // =================================
        // DELETE DOCTOR
        // =================================

        boolean deleted =
                doctorService.deleteDoctor("D003");

        System.out.println(
                "\nDelete Result: " + deleted
        );


        // =================================
        // CHECK AGAIN
        // =================================

        Doctor deletedDoctor =
                doctorService.findDoctorById("D003");

        if (deletedDoctor == null) {

            System.out.println(
                    "Doctor D003 no longer exists."
            );
        }
    }
}