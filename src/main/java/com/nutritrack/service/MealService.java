package com.nutritrack.service;

import com.nutritrack.model.FoodItem;
import com.nutritrack.model.MealEntry;
import com.nutritrack.repository.FoodItemRepository;
import com.nutritrack.repository.MealEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealService {

    @Autowired
    private MealEntryRepository mealEntryRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private GeminiService geminiService;

    @Value("${app.upload.dir:./data/uploads}")
    private String uploadDir;

    public Map<String, Object> analyzeAndSave(Long userId, String mealType, String itemName) {
        Map<String, Object> result = new HashMap<>();

        Optional<FoodItem> opt = foodItemRepository.findByNameIgnoreCase(itemName.trim());

        if (opt.isEmpty()) {
            result.put("found", false);
            result.put("message", "Item '" + itemName + "' not found in database.");
            return result;
        }

        FoodItem food = opt.get();
        MealEntry entry = new MealEntry();
        entry.setUserId(userId);
        entry.setDate(LocalDate.now());
        entry.setMealType(mealType);
        entry.setItemName(food.getName());
        entry.setCalories(food.getCalories());
        entry.setItemType(food.getType());

        String suggestion = null;
        if ("Unhealthy".equals(food.getType())) {
            suggestion = geminiService.getSuggestion(food.getName());
            entry.setAiSuggestion(suggestion);
        }

        mealEntryRepository.save(entry);

        result.put("found", true);
        result.put("itemName", food.getName());
        result.put("calories", food.getCalories());
        result.put("type", food.getType());
        result.put("suggestion", suggestion);
        result.put("entryId", entry.getId());
        return result;
    }

    public List<MealEntry> getTodayMeals(Long userId) {
        return mealEntryRepository.findByUserIdAndDateOrderByCreatedAtAsc(userId, LocalDate.now());
    }

    public int getTodayCalories(Long userId) {
        return getTodayMeals(userId).stream().mapToInt(MealEntry::getCalories).sum();
    }

    public Map<String, Object> getWeeklyReport(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(6);
        List<MealEntry> entries = mealEntryRepository.findByUserIdAndDateBetween(userId, weekAgo, today);

        // Group by date
        Map<String, Integer> dailyCalories = new TreeMap<>();
        Map<String, Long> healthyCount = new TreeMap<>();
        Map<String, Long> unhealthyCount = new TreeMap<>();

        for (LocalDate d = weekAgo; !d.isAfter(today); d = d.plusDays(1)) {
            dailyCalories.put(d.toString(), 0);
            healthyCount.put(d.toString(), 0L);
            unhealthyCount.put(d.toString(), 0L);
        }

        for (MealEntry e : entries) {
            String date = e.getDate().toString();
            dailyCalories.merge(date, e.getCalories(), Integer::sum);
            if ("Healthy".equals(e.getItemType()))
                healthyCount.merge(date, 1L, Long::sum);
            else
                unhealthyCount.merge(date, 1L, Long::sum);
        }

        Map<String, Object> report = new HashMap<>();
        report.put("dailyCalories", dailyCalories);
        report.put("healthyCount", healthyCount);
        report.put("unhealthyCount", unhealthyCount);
        report.put("totalEntries", entries.size());
        report.put("avgCalories", entries.isEmpty() ? 0 :
            entries.stream().mapToInt(MealEntry::getCalories).sum() / 7);
        return report;
    }

    public List<MealEntry> getAllUserMeals(Long userId) {
        return mealEntryRepository.findByUserIdOrderByDateDescCreatedAtDesc(userId);
    }

    public List<MealEntry> getSharedFeed() {
        return mealEntryRepository.findAllSharedEntries();
    }

    public List<LocalDate> getDatesWithMeals(Long userId) {
        return mealEntryRepository.findDistinctDatesByUserId(userId);
    }

    public MealEntry getMealById(Long id) {
        return mealEntryRepository.findById(id).orElse(null);
    }

    public MealEntry updateMealPost(Long entryId, Long userId, String caption, boolean shared, MultipartFile photo) throws IOException {
        MealEntry entry = mealEntryRepository.findById(entryId).orElse(null);
        if (entry == null || !entry.getUserId().equals(userId)) return null;

        entry.setCaption(caption);
        entry.setShared(shared);

        if (photo != null && !photo.isEmpty()) {
            String filename = "meal_" + entryId + "_" + System.currentTimeMillis() +
                    getExtension(photo.getOriginalFilename());
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);
            Path dest = dir.resolve(filename);
            photo.transferTo(dest.toFile());
            entry.setPhotoPath("/uploads/" + filename);
        }

        return mealEntryRepository.save(entry);
    }

    public void deleteMeal(Long id, Long userId) {
        MealEntry entry = mealEntryRepository.findById(id).orElse(null);
        if (entry != null && entry.getUserId().equals(userId)) {
            mealEntryRepository.delete(entry);
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return ".jpg";
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot) : ".jpg";
    }

    public List<MealEntry> getMealsForDate(Long userId, LocalDate date) {
        return mealEntryRepository.findByUserIdAndDateOrderByMealTypeAsc(userId, date);
    }
}
