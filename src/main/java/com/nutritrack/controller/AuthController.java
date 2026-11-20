package com.nutritrack.controller;

import com.nutritrack.model.User;
import com.nutritrack.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/dashboard";
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/dashboard";
        return "forward:/login.html";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("userId") != null) return "redirect:/dashboard";
        return "forward:/register.html";
    }

    @PostMapping("/api/register")
    @ResponseBody
    public String register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/api/login")
    @ResponseBody
    public String login(@RequestBody java.util.Map<String, String> body, HttpSession session) {
        Optional<User> opt = userService.login(body.get("username"), body.get("password"));
        if (opt.isPresent()) {
            User u = opt.get();
            session.setAttribute("userId", u.getId());
            session.setAttribute("userName", u.getName());
            return "ok";
        }
        return "Invalid credentials.";
    }

    @PostMapping("/api/logout")
    @ResponseBody
    public String logout(HttpSession session) {
        session.invalidate();
        return "ok";
    }

    @DeleteMapping("/api/account")
    @ResponseBody
    public String deleteAccount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "Not logged in.";
        userService.deleteUser(userId);
        session.invalidate();
        return "ok";
    }

    @GetMapping("/api/me")
    @ResponseBody
    public Object getMe(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "not_logged_in";
        return userService.findById(userId).orElse(null);
    }
}
