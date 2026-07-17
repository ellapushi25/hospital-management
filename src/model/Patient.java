package model;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;
    private String address;
    private String disease;
    private String admitDate;

    public Patient(int id, String name, int age, String gender, String phone,
                   String address, String disease, String admitDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.disease = disease;
        this.admitDate = admitDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getDisease() { return disease; }
    public String getAdmitDate() { return admitDate; }

    @Override
    public String toString() {
        return String.format("ID:%-4d | %-20s | Age:%-3d | %-6s | %-12s | %-15s | %-15s | Admitted:%s",
                id, name, age, gender, phone, address, disease, admitDate);
    }
}
