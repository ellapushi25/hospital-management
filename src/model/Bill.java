package model;

public class Bill {
    private int id;
    private int patientId;
    private String patientName; // filled in via JOIN, for display
    private String description;
    private double amount;
    private boolean paid;
    private String billDate;

    public Bill(int id, int patientId, String patientName, String description,
                double amount, boolean paid, String billDate) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.description = description;
        this.amount = amount;
        this.paid = paid;
        this.billDate = billDate;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public double getAmount() { return amount; }
    public boolean isPaid() { return paid; }

    @Override
    public String toString() {
        return String.format("ID:%-4d | Patient: %-20s | %-25s | $%-10.2f | %-6s | %s",
                id, patientName, description, amount, paid ? "PAID" : "UNPAID", billDate);
    }
}
