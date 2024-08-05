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
        return this.quizService.getQuestionTagRequests(userId, password, questionId);
    }

    @PutMapping("/updateQuestion/{userId}/{password}/{questionId}/{body}/{answer0}/{answer1}/{answer2}/{answer3}/{answerIndex}/{difficulty}")
    public void updateQuestion(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId,
                               @PathVariable String body, @PathVariable String answer0,
                               @PathVariable String answer1, @PathVariable String answer2, @PathVariable String answer3,
                               @PathVariable int answerIndex, @PathVariable String difficulty){
        this.quizService.updateQuestion(userId, password, questionId, body, answer0, answer1, answer2, answer3, answerIndex, difficulty);
    }

    @DeleteMapping("/deleteQuestion/{userId}/{password}/{questionId}")
    public void deleteQuestion(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId){
        this.quizService.deleteQuestion(userId, password, questionId);
    }

    @PutMapping("/updateTest/{userId}/{password}/{currName}/{newName}/{tag}")
    public void updateTest(@PathVariable int userId, @PathVariable String password, @PathVariable String currName,
                           @PathVariable String newName, @PathVariable String tag){
        this.quizService.updateTest(userId, password, currName, newName, tag);
    }
    @DeleteMapping("/deleteTest/{userId}/{password}/{name}")
    public void deleteTest(@PathVariable int userId, @PathVariable String password, @PathVariable String name){
        this.quizService.deleteTest(userId, password, name);
    }

    @PostMapping("/questionRequestTag/{userId}/{password}/{questionId}/{tag}")
    public void questionRequestTag(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId,
                                   @PathVariable String tag){
        //ToDo
    }

    @PostMapping("/testRequestAddQuestion/{userId}/{password}/{testId}/{questionId}/{tag}")
    public void testRequestAddQuestion(@PathVariable int userId, @PathVariable String password, @PathVariable int testId,
                                       @PathVariable int questionId, @PathVariable String tag){
        //ToDo
    }

    @DeleteMapping("/evaluateQuestionTagReq/{userId}/{password}/{questionId}/{tag}/{requesterId}/{approve}")
    public void evaluateQuestionTagReq(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId,
                                       @PathVariable String tag, @PathVariable int requesterId, @PathVariable long approve){
        //If approve is true, will add the tag to the question, else will only delete the request
        //ToDo
    }

    @DeleteMapping("/evaluateTestAddReq/{userId}/{password}/{testId}/{questionId}")
    public void evaluateTestAddReq(@PathVariable int userId, @PathVariable String password, @PathVariable int testId,
                                   @PathVariable int questionId){
        //YOU MAY WANT TO REMOVE THE TRIGGER WHICH RELEASES OTHER ADD REQUESTS TOO, AND DELETE ALL REQUEST AT ONCE IN
        //REPOSITORY LEVEL
        //ToDo
    }

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