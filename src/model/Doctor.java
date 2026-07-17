package model;

public class Doctor {
    private int id;
    private String name;
    private String specialization;
    private String phone;

    public Doctor(int id, String name, String specialization, String phone) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getPhone() { return phone; }

    @Override
    public String toString() {
        return String.format("ID:%-4d | Dr. %-20s | %-20s | %-12s", id, name, specialization, phone);
    }
}
