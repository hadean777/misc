package com.example.loginapp.repository;

import com.example.loginapp.model.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
}