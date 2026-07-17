package dao;

import db.Database;
import model.Bill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {

    public int addBill(int patientId, String description, double amount, String billDate) throws SQLException {
        String sql = "INSERT INTO bills (patient_id, description, amount, paid, bill_date) VALUES (?, ?, ?, 0, ?)";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, patientId);
            stmt.setString(2, description);
            stmt.setDouble(3, amount);
            stmt.setString(4, billDate);
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    private static final String SELECT_WITH_NAME =
            "SELECT b.id, b.patient_id, p.name AS patient_name, b.description, b.amount, b.paid, b.bill_date " +
            "FROM bills b JOIN patients p ON b.patient_id = p.id ";

    public List<Bill> getAllBills() throws SQLException {
        List<Bill> list = new ArrayList<>();
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_WITH_NAME + "ORDER BY b.id")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Bill> getUnpaidBills() throws SQLException {
        List<Bill> list = new ArrayList<>();
        try (Statement stmt = Database.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_WITH_NAME + "WHERE b.paid = 0 ORDER BY b.id")) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean markAsPaid(int billId) throws SQLException {
        String sql = "UPDATE bills SET paid = 1 WHERE id = ?";
        try (PreparedStatement stmt = Database.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, billId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Bill mapRow(ResultSet rs) throws SQLException {
        return new Bill(
                rs.getInt("id"),
                rs.getInt("patient_id"),
                rs.getString("patient_name"),
                rs.getString("description"),
                rs.getDouble("amount"),
                rs.getInt("paid") == 1,
                rs.getString("bill_date")
        );
    }
}
