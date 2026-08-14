package model;

public class Doctor extends User {
    private String specialization;
    private String phone;
    private double consultationFee;

    public Doctor(String id, String name, String email, String password, String phone, String specialization, double consultationFee) {
        super(id, name, email, password, "DOCTOR");

        this.phone = phone;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getPhone() {
        return phone;
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }
}