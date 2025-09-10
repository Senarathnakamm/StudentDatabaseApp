package com.studentapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // --- IMPORTANT: UPDATE THESE VALUES FOR YOUR DATABASE ---
    private String jdbcURL = "jdbc:mysql://localhost:3306/studentdb";
    private String jdbcUsername = "root";
    private String jdbcPassword = "root";
    // ---------------------------------------------------------

    /**
     * Establishes a connection to the database
     * @return a Connection object.
     */
    private Connection getConnection() throws SQLException {
        try {
            // Explicitly load the driver for better compatibility
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    /**
     * Adds a new student to the database using a PreparedStatement.
     * @param student The student object to add.
     * @return true if the student was added successfully, false otherwise.
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (name, age) VALUES (?, ?)";
        boolean rowAdded = false;

        // Using try-with-resources to ensure resources are automatically closed
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rowAdded = true;
                System.out.println("Student '" + student.getName() + "' added successfully.");
                
                // Retrieve the generated ID if needed
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        return rowAdded;
    }

    /**
     * Retrieves all students from the database.
     * @return a List of Student objects.
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) { // Uses executeQuery() for SELECT

            // The ResultSet holds the query results
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age"); 
                students.add(new Student(id, name, age));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return students;
    }

    /**
     * Updates an existing student's information.
     * @param student The student object with updated details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, age = ? WHERE id = ?";
        boolean rowUpdated = false;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getName());
            pstmt.setInt(2, student.getAge());
            pstmt.setInt(3, student.getId());

            // executeUpdate() is used for UPDATE statements.
            rowUpdated = pstmt.executeUpdate() > 0;
            if(rowUpdated) {
                System.out.println("Student with ID " + student.getId() + " updated successfully.");
            } else {
                 System.out.println("Could not find student with ID " + student.getId() + " to update.");
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return rowUpdated;
    }

    /**
     * Deletes a student from the database by their ID.
     * @param id The ID of the student to delete.
     * @return true if the deletion was successful, false otherwise.
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        boolean rowDeleted = false;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            
            // executeUpdate() is also used for DELETE statements.
            rowDeleted = pstmt.executeUpdate() > 0;
            if(rowDeleted) {
                System.out.println("Student with ID " + id + " deleted successfully.");
            } else {
                System.out.println("Could not find student with ID " + id + " to delete.");
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }
        return rowDeleted;
    }

    /**
     * Handles and prints details of an SQLException.
     * @param e The SQLException that occurred.
     */
    private void handleSQLException(SQLException e) {
        System.err.println("Database Error:");
        System.err.println("Message: " + e.getMessage());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("SQL State: " + e.getSQLState());
        e.printStackTrace(); // Added stack trace for better debugging
    }
}