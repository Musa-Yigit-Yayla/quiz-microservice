package com.example.quiz_service.service;
import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.lang.Exception;

@Service
public class QuizServiceService{
    private UserRepository userRepository;

    @Autowired
    public QuizServiceService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public QuestionDto getQuestion(String topic, String difficulty){
        return null;
    }
    public String createUser() throws Exception{
        try {
            return this.userRepository.addUser();
        }
        catch(Exception ex){
            throw ex;
        }
    }

    public String healthCheck() {
        return this.userRepository.healthCheck();
    }
}