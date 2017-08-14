package com.tody.datamodel;

import javafx.collections.FXCollections;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tody_ on 06/06/2017.
 */
public class Datasource {
    private static final String DB_NAME = "HospitalManagement.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
    private static Datasource instance = new Datasource();
    private Connection conn;
    private PreparedStatement queryLogin;
    private PreparedStatement queryFrontDeskUser;
    private PreparedStatement queryDepartmentAdmin;
    private PreparedStatement storeOPID;
    private PreparedStatement queryOPID;
    private PreparedStatement queryDoctors;
    private PreparedStatement storePatient;
    private PreparedStatement storeTreatment;
    private PreparedStatement queryPatientByIPID;
    private PreparedStatement queryAllPatients;
    private PreparedStatement queryTreatment;
    private PreparedStatement queryDoctor;
    private PreparedStatement storeUser;
    private PreparedStatement deleteFromUsers;
    private PreparedStatement queryAllUsers;
    private PreparedStatement updateFromUsers;
    private Statement createTable;

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    /* creates database connection and prepared statements */
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            createTable = conn.createStatement();
            createTable.execute("CREATE TABLE IF NOT EXISTS DepartmentSecretary\n" +
                    "(\n" +
                    "    id INTEGER PRIMARY KEY,\n" +
                    "    Name TEXT NOT NULL,\n" +
                    "    Password TEXT NOT NULL\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS Doctors\n" +
                    "(\n" +
                    "    id INTEGER PRIMARY KEY,\n" +
                    "    Name TEXT NOT NULL,\n" +
                    "    Speciality TEXT NOT NULL,\n" +
                    "    Password TEXT NOT NULL\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS FrontDeskUser\n" +
                    "(\n" +
                    "    id INTEGER PRIMARY KEY,\n" +
                    "    Name TEXT,\n" +
                    "    Password TEXT NOT NULL\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS \"op\" (\n" +
                    "\t`opid`\tINTEGER NOT NULL,\n" +
                    "\t`Name`\tTEXT,\n" +
                    "\t`Reason`\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(`opid`)\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS Patients\n" +
                    "(\n" +
                    "    IP_id TEXT PRIMARY KEY,\n" +
                    "    Name TEXT,\n" +
                    "    Age INTEGER,\n" +
                    "    Address TEXT,\n" +
                    "    Phone TEXT\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS Treatment\n" +
                    "(\n" +
                    "    IP_id TEXT NOT NULL,\n" +
                    "    Doc_id INTEGER,\n" +
                    "    Details TEXT,\n" +
                    "    Date TEXT NOT NULL\n" +
                    ")");
            createTable.execute("CREATE TABLE IF NOT EXISTS `users` (\n" +
                    "\t`id`\tINTEGER,\n" +
                    "\t`password`\tTEXT NOT NULL,\n" +
                    "\t`user_type`\tTEXT NOT NULL,\n" +
                    "\tPRIMARY KEY(`id`)\n" +
                    ")");
            createTable.execute("CREATE VIEW IF NOT EXISTS USERSVIEW AS\n" +
                    "SELECT Doctors.id AS id, Doctors.Name AS name, Doctors.Password AS password FROM Doctors\n" +
                    "UNION\n" +
                    "SELECT FrontDeskUser.id AS id, FrontDeskUser.Name AS name, FrontDeskUser.Password AS password FROM FrontDeskUser\n" +
                    "UNION\n" +
                    "SELECT DepartmentSecretary.id AS id, DepartmentSecretary.Name AS name, DepartmentSecretary.Password AS password \n" +
                    "FROM DepartmentSecretary");
            createTable.execute("CREATE VIEW IF NOT EXISTS ALLUSERS AS\n" +
                    "SELECT USERSVIEW.id, USERSVIEW.Name, USERSVIEW.Password, users.user_type FROM\n" +
                    "USERSVIEW, users\n" +
                    "WHERE\n" +
                    "USERSVIEW.id = users.id");

            queryLogin = conn.prepareStatement("SELECT * FROM ALLUSERS WHERE id=? AND password =?");
            queryFrontDeskUser = conn.prepareStatement("SELECT * FROM FrontDeskUser WHERE id=?");
            queryDepartmentAdmin = conn.prepareStatement("SELECT * FROM DepartmentSecretary WHERE id=?");
            queryDoctor = conn.prepareStatement("SELECT  * FROM Doctors WHERE id=?");
            storeOPID = conn.prepareStatement("INSERT INTO op (opid, Name, Reason) VALUES (?,?,?)");
            queryOPID = conn.prepareStatement("SELECT * FROM op WHERE opid=?");
            queryDoctors = conn.prepareStatement("SELECT * FROM Doctors");
            storePatient = conn.prepareStatement("INSERT INTO Patients(IP_id, Name, Age, Address, Phone) VALUES (?,?,?,?,?)");
            queryPatientByIPID = conn.prepareStatement("SELECT Name, Age, Address, Phone FROM  Patients WHERE IP_id =?");
            storeTreatment = conn.prepareStatement("INSERT INTO Treatment(IP_id, Doc_id, Details, Date) VALUES (?,?,?,?)");
            queryTreatment = conn.prepareStatement("SELECT * FROM Treatment WHERE IP_id = ?");
            queryAllPatients = conn.prepareStatement("SELECT * FROM Patients");
            storeUser = conn.prepareStatement("INSERT INTO users(id, password, user_type) VALUES (?,?,?)");
            queryAllUsers = conn.prepareStatement("SELECT * FROM ALLUSERS");
            deleteFromUsers = conn.prepareStatement("DELETE FROM users WHERE id=?");
            updateFromUsers = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?");

            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to the database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //closes all prepared statements and database connection
    public void close() {
        try {
            if (createTable != null) {
                createTable.close();
            }
            if (queryLogin != null) {
                queryLogin.close();
            }
            if (queryFrontDeskUser != null) {
                queryFrontDeskUser.close();
            }
            if (queryDepartmentAdmin != null) {
                queryDepartmentAdmin.close();
            }
            if (storeOPID != null) {
                storeOPID.close();
            }
            if (queryOPID != null) {
                queryOPID.close();
            }
            if (queryDoctor != null) {
                queryDoctor.close();
            }
            if (queryDoctors != null) {
                queryDoctors.close();
            }
            if (storePatient != null) {
                storePatient.close();
            }
            if (storeTreatment != null) {
                storeTreatment.close();
            }
            if (queryPatientByIPID != null) {
                queryPatientByIPID.close();
            }
            if (queryAllPatients != null) {
                queryAllPatients.close();
            }
            if (queryTreatment != null) {
                queryTreatment.close();
            }
            if (storeUser != null) {
                storeUser.close();
            }
            if (queryAllUsers != null) {
                queryAllUsers.close();
            }
            if (deleteFromUsers != null) {
                deleteFromUsers.close();
            }
            if (updateFromUsers != null) {
                updateFromUsers.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    // to validate user login
    public boolean validate(String id, String password, String userType) {
        boolean status = false;
        try {
            queryLogin.setString(1, id);
            queryLogin.setString(2, password);
            ResultSet result = queryLogin.executeQuery();
            while (result.next()) {
                if (userType.equalsIgnoreCase(result.getString(4))) {
                    status = true;
                } else {
                    status = false;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validating user " + e.getMessage());
            return false;
        }
        return status;
    }

    public ArrayList<String> validateOPID(int id) {
        try {
            queryOPID.setInt(1, id);
            ResultSet result = queryOPID.executeQuery();
            ArrayList op = new ArrayList();
            while (result.next()) {
                String name = result.getString(2);
                op.add(name);
                String reason = result.getString(3);
                op.add(reason);
            }
            return op;
        } catch (SQLException e) {
            System.out.println("Error validating OPID " + e.getMessage());
            return null;
        }

    }

    public FrontDeskUser queryFrontDeskUser(String userId) {
        try {
            queryFrontDeskUser.setString(1, userId);

            ResultSet results = queryFrontDeskUser.executeQuery();
            FrontDeskUser user = new FrontDeskUser();

            while (results.next()) {
                user.setId(results.getInt(1));
                user.setName(results.getString(2));
                user.setPassword(results.getString(3));
            }
            return user;

        } catch (SQLException e) {
            System.out.println("QueryUser failed! " + e.getMessage());
            return null;

        }

    }

    public DeptAdmin queryDeptAdmin(String userId) {
        try {
            queryDepartmentAdmin.setString(1, userId);

            ResultSet results = queryDepartmentAdmin.executeQuery();
            DeptAdmin user = new DeptAdmin();

            while (results.next()) {
                user.setId(results.getInt(1));
                user.setName(results.getString(2));
                user.setPassword(results.getString(3));
            }
            return user;

        } catch (SQLException e) {
            System.out.println("Querying user failed! " + e.getMessage());
            return null;

        }
    }

    public Doctor queryDoctor(String id) {
        try {
            queryDoctor.setString(1, id);

            Doctor doc = new Doctor();
            ResultSet result = queryDoctor.executeQuery();
            result.next();
            doc.setId(result.getInt(1));
            doc.setName(result.getString(2));
            doc.setSpeciality(result.getString(3));

            return doc;

        } catch (SQLException e) {
            System.out.println("Error querying Doc " + e.getMessage());
            return null;
        }
    }

    public List<Doctor> queryDoctors() {
        try {
            ResultSet results = queryDoctors.executeQuery();
            List<Doctor> doctors = new ArrayList<>();

            while (results.next()) {
                Doctor doc = new Doctor();
                doc.setId(results.getInt(1));
                doc.setName(results.getString(2));
                doc.setSpeciality(results.getString(3));

                doctors.add(doc);
            }
            return doctors;

        } catch (SQLException e) {
            System.out.println("Error querying doctors " + e.getMessage());
            return null;
        }
    }

    public boolean storeOPID(int opid, String name, String reason) {
        boolean status = false;
        try {
            storeOPID.setInt(1, opid);
            storeOPID.setString(2, name);
            storeOPID.setString(3, reason);
            if (storeOPID.executeUpdate() == 1) {
                status = true;
            }
            return status;
        } catch (SQLException e) {
            System.out.println("Error storing OPID " + e.getMessage());
            return false;
        }

    }

    public boolean storePatient(String ip_id, String name, int age, String address, String phone) {
        boolean status = false;
        try {
            storePatient.setString(1, ip_id);
            storePatient.setString(2, name);
            storePatient.setInt(3, age);
            storePatient.setString(4, address);
            storePatient.setString(5, phone);
            if (storePatient.executeUpdate() == 1) {
                status = true;
            }
            return status;
        } catch (SQLException e) {
            System.out.println("Error storing Patient" + e.getMessage());
            return false;
        }
    }

    public boolean storeTreatment(String ip_id, int doc_id, String details, LocalDate date) {
        boolean status = false;
        try {
            storeTreatment.setString(1, ip_id);
            storeTreatment.setInt(2, doc_id);
            storeTreatment.setString(3, details);
            storeTreatment.setString(4, date.toString());
            if (storeTreatment.executeUpdate() == 1) {
                status = true;
            }
            return status;
        } catch (SQLException e) {
            System.out.println("Error storing treatment details " + e.getMessage());
            return false;
        }
    }

    public List<TreatmentDetails> queryTreatment(String ip_id) {
        try {
            List<TreatmentDetails> treatmentDetails = new ArrayList<>();
            queryTreatment.setString(1, ip_id);
            ResultSet resultSet = queryTreatment.executeQuery();

            while (resultSet.next()) {
                TreatmentDetails treatment = new TreatmentDetails();

                treatment.setIp_id(resultSet.getString(1));
                treatment.setDoc_id(resultSet.getInt(2));
                treatment.setDetails(resultSet.getString(3));
                treatment.setDate(resultSet.getString(4));

                treatmentDetails.add(treatment);
            }
            return treatmentDetails;

        } catch (SQLException e) {
            System.out.println("Error querying treatment details! " + e.getMessage());
            return null;
        }
    }

    public Patient queryPatient(String ip_id) {
        try {
            queryPatientByIPID.setString(1, ip_id);
            ResultSet result = queryPatientByIPID.executeQuery();
            Patient patient = new Patient();

            if (result.next()) {
                patient.setIp_id(ip_id);
                patient.setName(result.getString(1));
                patient.setAge(result.getInt(2));
                patient.setAddress(result.getString(3));
                patient.setPhone(result.getString(4));
                List<TreatmentDetails> treatment = queryTreatment(ip_id);

                patient.setTreatmentDetails(FXCollections.observableArrayList(treatment));

                return patient;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Unable to query patient " + e.getMessage());
            return null;
        }
    }

    public List<Patient> queryAllPatients() {
        try {
            List<Patient> patients = new ArrayList<>();
            ResultSet result = queryAllPatients.executeQuery();

            while (result.next()) {
                Patient patient = new Patient();
                patient.setIp_id(result.getString(1));
                patient.setName(result.getString(2));
                patient.setAge(result.getInt(3));
                patient.setAddress(result.getString(4));
                patient.setPhone(result.getString(5));

                patients.add(patient);
            }
            return patients;
        } catch (SQLException e) {
            System.out.println("Error retrieving patients from the database! " + e.getMessage());
            return null;
        }
    }

    public boolean addUser(String usrType, String name, int id, String password, String speciality) {
        boolean status = false;
        StringBuilder statement = new StringBuilder("INSERT INTO ");
        int t = 0;
        switch (usrType) {
            case "FD":
                statement.append("FrontDeskUser (id, Name, Password) VALUES (?,?,?)");
                t = 1;
                break;
            case "DA":
                statement.append("DepartmentSecretary (id, Name, Password) VALUES (?,?,?)");
                t = 1;
                break;
            case "DOC":
                statement.append("Doctors (id, Name, Speciality, Password) VALUES (?,?,?,?)");
                t = 3;
                break;
        }
        try (PreparedStatement addUser = conn.prepareStatement(statement.toString())) {
            if (t == 1) {
                addUser.setInt(1, id);
                addUser.setString(2, name);
                addUser.setString(3, password);
                int i = storeUser(id, password, usrType);
                if (addUser.executeUpdate() == 1 && i == 1) {
                    status = true;
                }
            } else {
                addUser.setInt(1, id);
                addUser.setString(2, name);
                addUser.setString(3, speciality);
                addUser.setString(4, password);

                int i = storeUser(id, password, usrType);
                if (addUser.executeUpdate() == 1 && i == 1) {
                    status = true;
                }
            }
            return status;

        } catch (SQLException e) {
            System.out.println("Error updating(adding user) database " + e.getMessage());
            System.out.println("SQL COMMAND :" + statement.toString());
            return false;
        }


    }

    public boolean deleteUser(String usrType, int id) {
        boolean status = false;
        StringBuilder statement = new StringBuilder("DELETE FROM ");
        switch (usrType) {
            case "FD":
                statement.append("FrontDeskUser WHERE id=?");
                break;
            case "DA":
                statement.append("DepartmentSecretary WHERE id=?");
                break;

        }
        try (PreparedStatement deleteUser = conn.prepareStatement(statement.toString())) {
            deleteUser.setInt(1, id);
            int i = deleteFromUsers(id);
            if (deleteUser.executeUpdate() == 1 && i == 1) {
                status = true;
            }
            return status;

        } catch (SQLException e) {
            System.out.println("Error updating database " + e.getMessage());
            System.out.println("SQL COMMAND :" + statement.toString());
            return false;
        }


    }

    public boolean updateUser(String usrType, String name, int id, String password) {
        boolean status = false;
        StringBuilder statement = new StringBuilder("UPDATE ");
        int t = 0;
        switch (usrType) {
            case "FD":
                statement.append("FrontDeskUser SET Name=?, Password=? WHERE id=?");
                t = 1;
                break;
            case "DA":
                statement.append("DepartmentSecretary SET Name=?, Password=? WHERE id=?");
                t = 0;
                break;
        }
        try (PreparedStatement updateUser = conn.prepareStatement(statement.toString())) {
            if (t == 1) {
                int i = setDetails(updateUser, name, password, id);

                if (updateUser.executeUpdate() == 1 && i == 1) {
                    status = true;
                }
            } else {
                int i = setDetails(updateUser, name, password, id);

                if (updateUser.executeUpdate() == 1 && i == 1) {
                    status = true;
                }
            }
            return status;
        } catch (SQLException e) {
            System.out.println("Error updating database " + e.getMessage());
            System.out.println("SQL COMMAND :" + statement.toString());
            return false;
        }
    }

    // convenience method to set data in a prepared statement
    private int setDetails(PreparedStatement s, String name, String pass, int id) {
        try {
            s.setString(1, name);
            s.setString(2, pass);
            s.setInt(3, id);

            int i = updateFromUsers(pass, id);
            return i;
        } catch (SQLException e) {
            return 0;
        }
    }


    //Store users in the USERS table
    private int storeUser(int id, String password, String userType) {
        int status = 0;
        try {
            storeUser.setInt(1, id);
            storeUser.setString(2, password);
            storeUser.setString(3, userType);

            if (storeUser.executeUpdate() == 1) {
                status = 1;
            }
            return status;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //delete from users table
    private int deleteFromUsers(int id) {
        int status = 0;
        try {
            deleteFromUsers.setInt(1, id);

            if (deleteFromUsers.executeUpdate() == 1) {
                status = 1;
            }
            return status;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    //update user on USERS table
    private int updateFromUsers(String password, int id) {
        int status = 0;
        try {
            updateFromUsers.setString(1, password);
            updateFromUsers.setInt(2, id);

            if (updateFromUsers.executeUpdate() == 1) {
                status = 1;
            }
            return status;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    // Queries all users from the database
    public List queryAllUsers() {
        try {
            List users = new ArrayList();
            ResultSet result = queryAllUsers.executeQuery();

            while (result.next()) {
                int id = result.getInt(1);
                String pass = result.getString(3);
                String userType = result.getString(4);
                switch (userType) {
                    case "FD":
                        FrontDeskUser frontDeskUser = queryFrontDeskUser(String.valueOf(id));
                        frontDeskUser.setPassword(pass);
                        users.add(frontDeskUser);
                        break;
                    case "DA":
                        DeptAdmin deptAdmin = queryDeptAdmin(String.valueOf(id));
                        deptAdmin.setPassword(pass);
                        users.add(deptAdmin);
                        break;
                    case "DOC":
                        Doctor doctor = queryDoctor(String.valueOf(id));
                        doctor.setPassword(pass);
                        users.add(doctor);
                        break;
                    default:
                        break;

                }
            }
            return users;
        } catch (SQLException e) {
            System.out.println("Error querying all users from the database " + e.getMessage());
            return null;
        }
    }
}
