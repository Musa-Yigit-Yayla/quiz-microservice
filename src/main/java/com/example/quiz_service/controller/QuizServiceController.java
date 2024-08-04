package com.example.quiz_service.controller;

import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.Dto.QuestionTagRequestDto;
import com.example.quiz_service.Dto.TestAddRequestDto;
import com.example.quiz_service.service.QuizServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
        String result = "xdddddaaddd";
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
        try {
            String result = this.quizService.createUser();
            return result;
        }
        catch(Exception ex){
            return ex.getMessage();
        }
    }
    @PostMapping("/addQuestion/{userId}/{password}/{body}/{answer0}/{answer1}/{answer2}/{answer3}/{answerIndex}/{difficulty}")
    public void addQuestion(@PathVariable("userId") int userId, @PathVariable("password") String password,
                            @PathVariable("body") String body,
                            @PathVariable("answer0") String answer0, @PathVariable("answer1") String answer1,
                            @PathVariable("answer2") String answer2, @PathVariable("answer3") String answer3,
                            @PathVariable("answerIndex") int answerIndex, @PathVariable("difficulty") String difficulty){
        this.quizService.addQuestion(userId, password, body, answer0, answer1, answer2, answer3, answerIndex, difficulty);
    }

    @PostMapping("/createTest/{userId}/{password}/{name}/{tag}")
    public void createTest(@PathVariable int userId, @PathVariable String password, @PathVariable String name,
                           @PathVariable String tag){
        this.quizService.createTest(userId, password, name, tag);
    }

    @GetMapping("/getTestAddRequests/{userId}/{password}/{testName}")
    public List<TestAddRequestDto> getTestAddRequests(@PathVariable int userId, @PathVariable String password,
                                                      @PathVariable String testName){
        return this.quizService.getTestAddRequests(userId, password, testName);
    }

    /**
     *
     * @param userId
     * @param password
     * @return the questions this user has created
     */
    @GetMapping("/getSelfQuestions/{userId}/{password}")
    public List<QuestionDto> getSelfQuestions(@PathVariable int userId, @PathVariable String password){
        return this.quizService.getSelfQuestions(userId, password);
    }

    /**
     *
     * @param userId
     * @param password
     * @param questionId a question which belongs to the given user
     * @return tag requests added for this question
     */
    @GetMapping("/getQuestionTagRequests/{userId}/{password}/{questionId}")
    public List<QuestionTagRequestDto> getQuestionTagRequests(@PathVariable int userId, @PathVariable String password,
                                                              @PathVariable int questionId){

    }

    @PostMapping()

    @GetMapping("/testdb")
    public String testDatabaseConnection() {
        try {

            return this.quizService.healthCheck();
        }
        catch (Exception e) {
            e.printStackTrace();
            return "Database connection failed: " + e.getMessage();
        }
    }

}