package dao;

import db.Database;
import model.Appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    public int bookAppointment(int patientId, int doctorId, String date) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, status) " +
                     "VALUES (?, ?, ?, 'Scheduled')";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, date);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    // JOIN with patients and doctors so we can display readable names, not just IDs
    private static final String SELECT_WITH_NAMES =
            "SELECT a.id, a.patient_id, a.doctor_id, p.name AS patient_name, " +
            "d.name AS doctor_name, a.appointment_date, a.status " +
            "FROM appointments a " +
            "JOIN patients p ON a.patient_id = p.id " +
            "JOIN doctors d ON a.doctor_id = d.id ";

    public List<Appointment> getAllAppointments() throws SQLException {
        List<Appointment> list = new ArrayList<>();
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_WITH_NAMES + "ORDER BY a.id")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean updateStatus(int appointmentId, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean cancelAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Appointment mapRow(ResultSet rs) throws SQLException {
        return new Appointment(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getInt("doctor_id"),
                rs.getString("patient_name"),
                rs.getString("doctor_name"),
                rs.getString("appointment_date"),
                rs.getString("status")
        );
    }
}
