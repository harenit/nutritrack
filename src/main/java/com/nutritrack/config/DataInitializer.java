package com.nutritrack.config;

import com.nutritrack.model.FoodItem;
import com.nutritrack.repository.FoodItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedFoodItems(FoodItemRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                // Healthy items
                List<FoodItem> items = List.of(
                    new FoodItem("apple", 52, "Healthy"),
                    new FoodItem("banana", 89, "Healthy"),
                    new FoodItem("milk", 42, "Healthy"),
                    new FoodItem("egg", 155, "Healthy"),
                    new FoodItem("salad", 33, "Healthy"),
                    new FoodItem("rice", 130, "Healthy"),
                    new FoodItem("chapati", 120, "Healthy"),
                    new FoodItem("dal", 116, "Healthy"),
                    new FoodItem("vegetables", 50, "Healthy"),
                    new FoodItem("oats", 68, "Healthy"),
                    new FoodItem("idli", 58, "Healthy"),
                    new FoodItem("dosa", 133, "Healthy"),
                    new FoodItem("curd", 61, "Healthy"),
                    // Unhealthy items
                    new FoodItem("pizza", 400, "Unhealthy"),
                    new FoodItem("burger", 295, "Unhealthy"),
                    new FoodItem("fries", 365, "Unhealthy"),
                    new FoodItem("chips", 536, "Unhealthy"),
                    new FoodItem("chocolate", 546, "Unhealthy"),
                    new FoodItem("cake", 350, "Unhealthy"),
                    new FoodItem("icecream", 207, "Unhealthy"),
                    new FoodItem("soda", 150, "Unhealthy"),
                    new FoodItem("fried rice", 330, "Unhealthy"),
                    new FoodItem("biryani", 500, "Unhealthy"),
                    new FoodItem("noodles", 300, "Unhealthy")
                );
                repo.saveAll(items);
                System.out.println("✅ Food items seeded into database.");
            }
        };
    }
}
