package com.example.quiz_service.controller;

import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.service.QuizServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quizservice")
class QuizServiceController{
    private QuizServiceService quizService = null;

    @Autowired //constructor injection
    public QuizServiceController(QuizServiceService quizServiceService) {
        this.quizService = quizServiceService;
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

    /**
     * @return a string in the format id:password for later login use of the newly created user
     */
    @PostMapping("/createUser")
    public String createUser(){
        String result = this.quizService.createUser();
        return result;
    }
}