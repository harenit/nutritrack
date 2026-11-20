package com.nutritrack.model;

import jakarta.persistence.*;

@Entity
@Table(name = "food_items")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private int calories;
    private String type; // Healthy / Unhealthy

    public FoodItem() {}

    public FoodItem(String name, int calories, String type) {
        this.name = name;
        this.calories = calories;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
