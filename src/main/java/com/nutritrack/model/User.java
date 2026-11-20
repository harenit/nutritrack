package com.nutritrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;
    private int age;
    private double height; // in meters
    private double weight; // in kg
    private String gender;
    private double activityMultiplier;

    public User() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getActivityMultiplier() { return activityMultiplier; }
    public void setActivityMultiplier(double activityMultiplier) { this.activityMultiplier = activityMultiplier; }

    public double calculateBMI() {
        if (height <= 0) return 0;
        return Math.round((weight / (height * height)) * 100.0) / 100.0;
    }

    public String getBMICategory() {
        double bmi = calculateBMI();
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    public int getDailyCalories() {
        double heightCm = height * 100;
        double bmr;
        if ("male".equalsIgnoreCase(gender))
            bmr = (10 * weight) + (6.25 * heightCm) - (5 * age) + 5;
        else
            bmr = (10 * weight) + (6.25 * heightCm) - (5 * age) - 161;
        return (int) (bmr * activityMultiplier);
    }
}
