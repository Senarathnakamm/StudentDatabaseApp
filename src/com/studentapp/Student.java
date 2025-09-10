package com.studentapp;

public class Student {
    private int id;
    private String name;
    private int age;

    /**
     * Constructor for creating a new student object to be saved in the database.
     * The database will auto-generate the ID.
     */
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Constructor for creating a student object from data retrieved from the database.
     */
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    // --- Getters and Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student [ID=" + id + ", Name=" + name + ", Age=" + age + "]";
    }
}