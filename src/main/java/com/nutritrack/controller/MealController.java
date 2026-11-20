package com.nutritrack.controller;

import com.nutritrack.model.MealEntry;
import com.nutritrack.service.MealService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    // Log a single food item
    @PostMapping("/log")
    public Map<String, Object> logMeal(@RequestBody Map<String, String> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Map.of("error", "Not logged in");
        return mealService.analyzeAndSave(userId, body.get("mealType"), body.get("itemName"));
    }

    // Get today's meals
    @GetMapping("/today")
    public Map<String, Object> today(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Map.of("error", "Not logged in");
        List<MealEntry> entries = mealService.getTodayMeals(userId);
        int total = entries.stream().mapToInt(MealEntry::getCalories).sum();
        return Map.of("entries", entries, "totalCalories", total);
    }

    // Weekly report data
    @GetMapping("/weekly")
    public Map<String, Object> weekly(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return Map.of("error", "Not logged in");
        return mealService.getWeeklyReport(userId);
    }

    // All user meals (for calendar)
    @GetMapping("/all")
    public List<MealEntry> allMeals(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return List.of();
        return mealService.getAllUserMeals(userId);
    }

    // Get meals for a specific date
    @GetMapping("/date/{date}")
    public List<MealEntry> mealsByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                        HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return List.of();
        return mealService.getMealsForDate(userId, date);
    }

    // Dates that have meal entries (for calendar highlighting)
    @GetMapping("/dates")
    public List<LocalDate> datesWithMeals(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return List.of();
        return mealService.getDatesWithMeals(userId);
    }

    // Community/shared feed
    @GetMapping("/community")
    public List<MealEntry> community() {
        return mealService.getSharedFeed();
    }

    // Update a meal entry (add photo, caption, share toggle)
    @PutMapping("/{id}/post")
    public MealEntry updatePost(@PathVariable Long id,
                                 @RequestParam(required = false) String caption,
                                 @RequestParam(defaultValue = "false") boolean shared,
                                 @RequestParam(required = false) MultipartFile photo,
                                 HttpSession session) throws Exception {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return null;
        return mealService.updateMealPost(id, userId, caption, shared, photo);
    }

    // Delete a meal entry
    @DeleteMapping("/{id}")
    public String deleteMeal(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "Not logged in";
        mealService.deleteMeal(id, userId);
        return "ok";
    }
}
