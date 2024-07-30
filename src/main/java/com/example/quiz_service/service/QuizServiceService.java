package com.example.quiz_service.service;
import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
public class QuizServiceService{
    private UserRepository userRepository;

    public QuizServiceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public QuestionDto getQuestion(String topic, String difficulty){
        return null;
    }
    public String createUser(){
        return this.userRepository.addUser();
    }
}