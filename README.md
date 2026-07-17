# Hospital Patient Management System

A console-based Java application for managing patients, doctors, appointments, and billing,
backed by a real SQLite database via JDBC.

## Requirements
- Java JDK 17 or newer (needs `javac`, not just `java`)
- No database server needed — SQLite runs as a local file (`hospital.db`), created automatically.

## Project structure
```
hospital-management/
├── lib/sqlite-jdbc.jar     # SQLite JDBC driver
├── schema.sql              # Database table definitions (auto-run on startup)
├── src/
│   ├── Main.java           # Console menu system
│   ├── db/Database.java    # Connection handling + schema setup
│   ├── model/              # Patient, Doctor, Appointment, Bill (plain data classes)
│   └── dao/                # PatientDAO, DoctorDAO, AppointmentDAO, BillDAO (JDBC CRUD logic)
├── compile.sh / compile.bat
└── run.sh / run.bat
```

## How to run

**Mac/Linux:**
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

**Windows:**
```cmd
compile.bat
run.bat
```

The first run creates `hospital.db` in the project folder and sets up all four tables automatically.

## Features

**1. Patient Management** — add, view all, search by name, update, delete
**2. Doctor Management** — add, view all, delete
**3. Appointments** — book (links a patient + doctor + date), view all (shows real names via SQL JOIN), update status, cancel
**4. Billing** — create a bill for a patient, view all bills, view only unpaid bills, mark as paid

## How the database layer works

- `Database.java` opens **one shared connection** to `hospital.db` and runs `schema.sql` on startup
  (using `CREATE TABLE IF NOT EXISTS`, so it's safe to run every time).
- Each entity has its own **DAO** (Data Access Object) class, e.g. `PatientDAO`, that contains all
  the SQL for that entity — this keeps database code separate from the menu/UI code in `Main.java`.
- All queries use **`PreparedStatement`** with `?` placeholders instead of building SQL strings by
  hand — this prevents SQL injection, which matters even in a learning project since it's the
  standard professional practice.
- Appointments and Bills use a **SQL JOIN** with the patients/doctors tables so you see readable
  names ("Dr. Sarah Lee") instead of just numeric IDs.

## Try it out — a sample walkthrough
1. `2` → `1` to add a doctor (e.g. name: `Sarah Lee`, specialization: `Cardiology`)
2. `1` → `1` to add a patient
3. `3` → `1` to book an appointment (use the patient ID and doctor ID from steps 1–2)
4. `4` → `1` to create a bill for that patient
5. `3` → `2` and `4` → `2` to see the appointment and bill with names filled in

## Extending it
Ideas if you want to keep building:
- Add input validation (e.g. reject empty names, negative ages)
- Add a "patient history" report combining appointments + bills for one patient
- Swap SQLite for MySQL/PostgreSQL (just change the connection URL and driver jar)
- Wrap it in a GUI (Swing) or a web interface (Spring Boot) instead of the console menu
