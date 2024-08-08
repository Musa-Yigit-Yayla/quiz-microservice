package com.example.quiz_service;

import com.example.quiz_service.repository.QuestionRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class QuizServiceApplication {

	public static void main(String[] args) {
		Logger tempLogger = LoggerFactory.getLogger(QuizServiceApplication.class);
		tempLogger.info("App.main scope is about to run the spring app");
		SpringApplication.run(QuizServiceApplication.class, args);
	}

}
