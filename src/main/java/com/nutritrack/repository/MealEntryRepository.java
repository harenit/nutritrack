package com.nutritrack.repository;

import com.nutritrack.model.MealEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MealEntryRepository extends JpaRepository<MealEntry, Long> {

    List<MealEntry> findByUserIdAndDateOrderByCreatedAtAsc(Long userId, LocalDate date);

    List<MealEntry> findByUserIdOrderByDateDescCreatedAtDesc(Long userId);

    @Query("SELECT m FROM MealEntry m WHERE m.userId = :userId AND m.date >= :from AND m.date <= :to ORDER BY m.date ASC")
    List<MealEntry> findByUserIdAndDateBetween(@Param("userId") Long userId,
                                                @Param("from") LocalDate from,
                                                @Param("to") LocalDate to);

    @Query("SELECT m FROM MealEntry m WHERE m.shared = true ORDER BY m.createdAt DESC")
    List<MealEntry> findAllSharedEntries();

    List<MealEntry> findByUserIdAndDateOrderByMealTypeAsc(Long userId, LocalDate date);

    @Query("SELECT DISTINCT m.date FROM MealEntry m WHERE m.userId = :userId ORDER BY m.date DESC")
    List<LocalDate> findDistinctDatesByUserId(@Param("userId") Long userId);
}
