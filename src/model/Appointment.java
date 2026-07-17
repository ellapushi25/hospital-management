package model;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private String patientName;  // filled in via JOIN, for display
    private String doctorName;   // filled in via JOIN, for display
    private String appointmentDate;
    private String status;

    public Appointment(int id, int patientId, int doctorId, String patientName,
                        String doctorName, String appointmentDate, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public String getAppointmentDate() { return appointmentDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("ID:%-4d | Patient: %-20s | Doctor: Dr. %-20s | %-12s | Status:%s",
                id, patientName, doctorName, appointmentDate, status);
    }
}
