package dao;

import db.Database;
import model.Patient;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PatientDAOTest {

    private static PatientDAO patientDAO;

    @BeforeAll
    static void setUp() {
        // Use an in-memory database only for tests, so we never touch hospital.db
        System.setProperty("db.url", "jdbc:sqlite::memory:");
        Database.initializeSchema();
        patientDAO = new PatientDAO();
    }

    @Test
    void addPatient_returnsGeneratedId() throws SQLException {
        int id = patientDAO.addPatient("Test Patient", 30, "Male", "1234567", "Address", "Flu", "2026-07-18");
        assertTrue(id > 0);
    }

    @Test
    void getPatientById_returnsCorrectPatient() throws SQLException {
        int id = patientDAO.addPatient("Ana Doe", 25, "Female", "555", "Tirana", "Cold", "2026-07-18");
        Patient p = patientDAO.getPatientById(id);
        assertNotNull(p);
        assertEquals("Ana Doe", p.getName());
        assertEquals(25, p.getAge());
    }

    @Test
    void getPatientById_returnsNullWhenNotFound() throws SQLException {
        Patient p = patientDAO.getPatientById(999999);
        assertNull(p);
    }

    @Test
    void deletePatient_removesPatient() throws SQLException {
        int id = patientDAO.addPatient("To Delete", 40, "Male", "111", "Addr", "X", "2026-07-18");
        boolean deleted = patientDAO.deletePatient(id);
        assertTrue(deleted);
        assertNull(patientDAO.getPatientById(id));
    }

    @Test
    void updatePatient_changesFields() throws SQLException {
        int id = patientDAO.addPatient("Old Name", 20, "Male", "000", "Addr", "X", "2026-07-18");
        boolean updated = patientDAO.updatePatient(id, "New Name", 21, "Male", "000", "Addr", "Y");
        assertTrue(updated);
        Patient p = patientDAO.getPatientById(id);
        assertEquals("New Name", p.getName());
        assertEquals(21, p.getAge());
    }
}