package com.example.loginapp.controller;

import com.example.loginapp.model.LoginAttempt;
import com.example.loginapp.repository.LoginAttemptRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpServletRequest request) {
        // Save login attempt to database
        try {
            if (username != null && password != null) {
                LoginAttempt attempt = new LoginAttempt(username, password, System.currentTimeMillis(), request.getRemoteAddr());
                loginAttemptRepository.save(attempt);
            }
        } catch (Exception e) {
            log.error("Error:", e);
        }
        // Redirect back to login page (no authentication check)
        return "redirect:/login";
    }
}