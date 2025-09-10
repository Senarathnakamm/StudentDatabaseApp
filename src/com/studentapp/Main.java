package com.studentapp;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n--- Student Database Management System ---");
            System.out.println("1. Add a new Student");
            System.out.println("2. View all Students");
            System.out.println("3. Update a Student's information");
            System.out.println("4. Delete a Student");
            System.out.println("5. Exit");
            System.out.print("Please enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        addStudent(scanner, studentDAO);
                        break;
                    case 2:
                        viewAllStudents(studentDAO);
                        break;
                    case 3:
                        updateStudent(scanner, studentDAO);
                        break;
                    case 4:
                        deleteStudent(scanner, studentDAO);
                        break;
                    case 5:
                        System.out.println("Exiting the application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
                choice = 0; // Reset choice to continue the loop
            }

        } while (choice != 5);

        scanner.close();
    }

    private static void addStudent(Scanner scanner, StudentDAO studentDAO) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        studentDAO.addStudent(new Student(name, age));
    }

    private static void viewAllStudents(StudentDAO studentDAO) {
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found in the database.");
        } else {
            System.out.println("\n--- List of All Students ---");
            for (Student student : students) {
                System.out.println(student.toString());
            }
            System.out.println("----------------------------");
        }
    }

    private static void updateStudent(Scanner scanner, StudentDAO studentDAO) {
        System.out.print("Enter the ID of the student to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter the new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter the new age: ");
        int newAge = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        studentDAO.updateStudent(new Student(id, newName, newAge));
    }

    private static void deleteStudent(Scanner scanner, StudentDAO studentDAO) {
        System.out.print("Enter the ID of the student to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        studentDAO.deleteStudent(id);
    }
}