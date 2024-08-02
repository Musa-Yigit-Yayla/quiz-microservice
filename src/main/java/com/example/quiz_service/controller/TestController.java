package com.example.quiz_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/testing")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/testdb")
    public String testDatabaseConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "Database connection successful";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Database connection failed: " + e.getMessage();
        }
    }
}
