package com.example.demo.service;

import com.example.demo.entity.LoginAttempt;
import com.example.demo.repository.LoginAttemptRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    private final LoginAttemptRepository repository;

    public LoginAttemptService(LoginAttemptRepository repository) {
        this.repository = repository;
    }

    public void saveFailedAttempt(String username, String passwordAttempt, HttpServletRequest request) {

        final long now = System.currentTimeMillis();

        LoginAttempt attempt = new LoginAttempt();
        attempt.setUsername(username);
        attempt.setPasswd(passwordAttempt);
        attempt.setAttemptTime(now);
        attempt.setIpAddress(request.getRemoteAddr());

        repository.save(attempt);
    }
}
