package model;

public class Patient extends User {
    private int age;
    private String gender;
    private String phone;
    private String address;

    public Patient(String id, String name, String email, String password, int age, String gender, String phone, String address) {
        super(id, name, email, password, "PATIENT");

        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}