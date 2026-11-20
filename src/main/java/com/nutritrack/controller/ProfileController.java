package com.nutritrack.controller;

import com.nutritrack.model.User;
import com.nutritrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @PutMapping
    public Object updateProfile(@RequestBody User updated, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "Not logged in";

        Optional<User> opt = userService.findById(userId);
        if (opt.isEmpty()) return "User not found";

        User existing = opt.get();
        existing.setName(updated.getName());
        existing.setAge(updated.getAge());
        existing.setHeight(updated.getHeight());
        existing.setWeight(updated.getWeight());
        existing.setGender(updated.getGender());
        existing.setActivityMultiplier(updated.getActivityMultiplier());

        return userService.updateUser(existing);
    }

    @PutMapping("/password")
    public String changePassword(@RequestBody Map<String, String> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "Not logged in";

        Optional<User> opt = userService.findById(userId);
        if (opt.isEmpty()) return "User not found";

        User u = opt.get();
        u.setPassword(body.get("password"));
        userService.updateUser(u);
        return "ok";
    }
}
