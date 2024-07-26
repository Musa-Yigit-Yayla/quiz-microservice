package com.example.quiz_service.controller;

import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.service.QuizServiceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizservice")
class QuizServiceController{
    private QuizServiceService quizService = null;

    public QuizServiceController() {
        this.quizService = new QuizServiceService();
    }

    @GetMapping("/getQuestion/{topic}/{difficulty}")
    public String getQuestion(@PathVariable("topic") String topic, @PathVariable("difficulty") String difficulty){
        String result = "xddd";
        QuestionDto response = this.quizService.getQuestion(topic, difficulty);

        if(response != null){
            result = response.toString();
        }
        return result;
    }
}