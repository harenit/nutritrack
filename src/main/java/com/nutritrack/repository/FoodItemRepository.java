package com.nutritrack.repository;

import com.nutritrack.model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Optional<FoodItem> findByNameIgnoreCase(String name);
}
