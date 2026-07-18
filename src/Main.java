import dao.*;
import db.Database;
import model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final BillDAO billDAO = new BillDAO();

    public static void main(String[] args) {
        Database.initializeSchema();
        System.out.println("=========================================");
        System.out.println(" HOSPITAL PATIENT MANAGEMENT SYSTEM");
        System.out.println("=========================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> patientMenu();
                    case 2 -> doctorMenu();
                    case 3 -> appointmentMenu();
                    case 4 -> billingMenu();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option, try again.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }

        Database.close();
        System.out.println("Goodbye!");
    }

    private static void printMainMenu() {
        System.out.println("\n----------- MAIN MENU -----------");
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointments");
        System.out.println("4. Billing");
        System.out.println("0. Exit");
    }

    // ---------------- PATIENTS ----------------
    private static void patientMenu() throws SQLException {
        System.out.println("\n-- Patient Management --");
        System.out.println("1. Add Patient  2. View All  3. Search by Name  4. Update  5. Delete  0. Back");
        int c = readInt("Choose: ");
        switch (c) {
            case 1 -> {
                String name = readNonEmptyString("Name: ");
                int age = readAge("Age: ");
                String gender = readNonEmptyString("Gender: ");
                String phone = readNonEmptyString("Phone: ");
                String address = readNonEmptyString("Address: ");
                String disease = readNonEmptyString("Disease/Reason for visit: ");
                String today = LocalDate.now().toString();
                int id = patientDAO.addPatient(name, age, gender, phone, address, disease, today);
                System.out.println("Patient added with ID: " + id);
            }
            case 2 -> {
                List<Patient> patients = patientDAO.getAllPatients();
                if (patients.isEmpty()) System.out.println("No patients found.");
                patients.forEach(System.out::println);
            }
            case 3 -> {
                String namePart = readString("Enter name (or part of it): ");
                List<Patient> results = patientDAO.searchByName(namePart);
                if (results.isEmpty()) System.out.println("No matches found.");
                results.forEach(System.out::println);
            }
            case 4 -> {
                int id = readInt("Patient ID to update: ");
                Patient existing = patientDAO.getPatientById(id);
                if (existing == null) {
                    System.out.println("Patient not found.");
                    return;
                }
                System.out.println("Leave blank to keep current value.");
                String name = readStringDefault("Name [" + existing.getName() + "]: ", existing.getName());
                int age = readIntDefault("Age [" + existing.getAge() + "]: ", existing.getAge());
                String gender = readStringDefault("Gender [" + existing.getGender() + "]: ", existing.getGender());
                String phone = readStringDefault("Phone [" + existing.getPhone() + "]: ", existing.getPhone());
                String address = readStringDefault("Address [" + existing.getAddress() + "]: ", existing.getAddress());
                String disease = readStringDefault("Disease [" + existing.getDisease() + "]: ", existing.getDisease());
                boolean ok = patientDAO.updatePatient(id, name, age, gender, phone, address, disease);
                System.out.println(ok ? "Updated." : "Update failed.");
            }
            case 5 -> {
                int id = readInt("Patient ID to delete: ");
                boolean ok = patientDAO.deletePatient(id);
                System.out.println(ok ? "Deleted." : "Patient not found.");
            }
            case 0 -> {
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ---------------- DOCTORS ----------------
    private static void doctorMenu() throws SQLException {
        System.out.println("\n-- Doctor Management --");
        System.out.println("1. Add Doctor  2. View All  3. Delete  0. Back");
        int c = readInt("Choose: ");
        switch (c) {
            case 1 -> {
                String name = readNonEmptyString("Name: ");
                String spec = readNonEmptyString("Specialization: ");
                String phone = readNonEmptyString("Phone: ");
                int id = doctorDAO.addDoctor(name, spec, phone);
                System.out.println("Doctor added with ID: " + id);
            }
            case 2 -> {
                List<Doctor> doctors = doctorDAO.getAllDoctors();
                if (doctors.isEmpty()) System.out.println("No doctors found.");
                doctors.forEach(System.out::println);
            }
            case 3 -> {
                int id = readInt("Doctor ID to delete: ");
                boolean ok = doctorDAO.deleteDoctor(id);
                System.out.println(ok ? "Deleted." : "Doctor not found.");
            }
            case 0 -> {
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ---------------- APPOINTMENTS ----------------
    private static void appointmentMenu() throws SQLException {
        System.out.println("\n-- Appointments --");
        System.out.println("1. Book Appointment  2. View All  3. Update Status  4. Cancel  0. Back");
        int c = readInt("Choose: ");
        switch (c) {
            case 1 -> {
                int patientId = readInt("Patient ID: ");
                if (patientDAO.getPatientById(patientId) == null) {
                    System.out.println("Patient not found.");
                    return;
                }
                int doctorId = readInt("Doctor ID: ");
                if (doctorDAO.getDoctorById(doctorId) == null) {
                    System.out.println("Doctor not found.");
                    return;
                }
                String date = readDate("Appointment date (YYYY-MM-DD): ");
                int id = appointmentDAO.bookAppointment(patientId, doctorId, date);
                System.out.println("Appointment booked with ID: " + id);
            }
            case 2 -> {
                List<Appointment> list = appointmentDAO.getAllAppointments();
                if (list.isEmpty()) System.out.println("No appointments found.");
                list.forEach(System.out::println);
            }
            case 3 -> {
                int id = readInt("Appointment ID: ");
                String status = readString("New status (Scheduled/Completed/Cancelled): ");
                boolean ok = appointmentDAO.updateStatus(id, status);
                System.out.println(ok ? "Status updated." : "Appointment not found.");
            }
            case 4 -> {
                int id = readInt("Appointment ID to cancel: ");
                boolean ok = appointmentDAO.cancelAppointment(id);
                System.out.println(ok ? "Cancelled." : "Appointment not found.");
            }
            case 0 -> {
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ---------------- BILLING ----------------
    private static void billingMenu() throws SQLException {
        System.out.println("\n-- Billing --");
        System.out.println("1. Create Bill  2. View All Bills  3. View Unpaid  4. Mark as Paid  0. Back");
        int c = readInt("Choose: ");
        switch (c) {
            case 1 -> {
                int patientId = readInt("Patient ID: ");
                if (patientDAO.getPatientById(patientId) == null) {
                    System.out.println("Patient not found.");
                    return;
                }
                String desc = readNonEmptyString("Description (e.g. Consultation, X-Ray): ");
                double amount = readDouble("Amount: $");
                String today = LocalDate.now().toString();
                int id = billDAO.addBill(patientId, desc, amount, today);
                System.out.println("Bill created with ID: " + id);
            }
            case 2 -> {
                List<Bill> bills = billDAO.getAllBills();
                if (bills.isEmpty()) System.out.println("No bills found.");
                bills.forEach(System.out::println);
            }
            case 3 -> {
                List<Bill> unpaid = billDAO.getUnpaidBills();
                if (unpaid.isEmpty()) System.out.println("No unpaid bills.");
                unpaid.forEach(System.out::println);
            }
            case 4 -> {
                int id = readInt("Bill ID to mark as paid: ");
                boolean ok = billDAO.markAsPaid(id);
                System.out.println(ok ? "Marked as paid." : "Bill not found.");
            }
            case 0 -> {
            }
            default -> System.out.println("Invalid option.");
        }
    }

    // ---------------- INPUT HELPERS ----------------
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static int readIntDefault(String prompt, int def) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return def;
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static String readStringDefault(String prompt, String def) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        return line.isEmpty() ? def : line;
    }

    // ---------------- NEW VALIDATION HELPERS ----------------
    private static String readDate(String prompt) {
        while (true) {
            String line = readString(prompt);
            try {
                LocalDate.parse(line);
                return line;
            } catch (Exception e) {
                System.out.println("Invalid date format. Use YYYY-MM-DD (e.g. 2026-07-20).");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            String line = readString(prompt);
            if (!line.isEmpty()) return line;
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readAge(String prompt) {
        while (true) {
            int age = readInt(prompt);
            if (age >= 0 && age <= 130) return age;
            System.out.println("Age must be between 0 and 130.");
        }
    }
}

