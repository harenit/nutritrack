package com.nutritrack.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/dashboard.html";
    }

    @GetMapping("/weekly")
    public String weekly(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/weekly.html";
    }

    @GetMapping("/calendar")
    public String calendar(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/calendar.html";
    }

    @GetMapping("/bmi")
    public String bmi(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/bmi.html";
    }

    @GetMapping("/community")
    public String community(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/community.html";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        return "forward:/profile.html";
    }
}
