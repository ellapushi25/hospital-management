package dao;

import db.Database;
import model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public int addPatient(String name, int age, String gender, String phone,
                           String address, String disease, String admitDate) throws SQLException {
        String sql = "INSERT INTO patients (name, age, gender, phone, address, disease, admit_date) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, disease);
            stmt.setString(7, admitDate);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Patient> getAllPatients() throws SQLException {
        String sql = "SELECT * FROM patients ORDER BY id";
        List<Patient> patients = new ArrayList<>();
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapRow(rs));
            }
        }
        return patients;
    }

    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public List<Patient> searchByName(String namePart) throws SQLException {
        String sql = "SELECT * FROM patients WHERE name LIKE ? ORDER BY id";
        List<Patient> patients = new ArrayList<>();
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + namePart + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) patients.add(mapRow(rs));
            }
        }
        return patients;
    }

    public boolean updatePatient(int id, String name, int age, String gender, String phone,
                                  String address, String disease) throws SQLException {
        String sql = "UPDATE patients SET name=?, age=?, gender=?, phone=?, address=?, disease=? WHERE id=?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, gender);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, disease);
            stmt.setInt(7, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Patient mapRow(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getString("disease"),
                rs.getString("admit_date")
        );
    }
}
