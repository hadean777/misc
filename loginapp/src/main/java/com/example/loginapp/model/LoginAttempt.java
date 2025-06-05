package com.example.loginapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LoginAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwd;
    private Long attemptTime;
    private String ipAddress;

    // Constructors
    public LoginAttempt() {
    }

    public LoginAttempt(String username, String passwd, Long attemptTime, String ipAddress) {
        this.username = username;
        this.passwd = passwd;
        this.attemptTime = attemptTime;
        this.ipAddress = ipAddress;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String password) {
        this.passwd = password;
    }

    public Long getAttemptTime() {
        return attemptTime;
    }

    public void setAttemptTime(Long attemptTime) {
        this.attemptTime = attemptTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}