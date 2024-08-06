package com.example.quiz_service.controller;

import com.example.quiz_service.Dto.*;
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
    public void addQuestion(@PathVariable int userId, @PathVariable String password,
                            @PathVariable String body, @PathVariable String answer0, @PathVariable String answer1,
                            @PathVariable String answer2, @PathVariable String answer3,
                            @PathVariable int answerIndex, @PathVariable String difficulty){
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

    /**
     *
     * @param userId
     * @param password
     * @param questionId
     * @param tag
     * if the user owns this question, directly adds the tag. Otherwise inserts a request regarding adding the tag.
     */
    @PostMapping("/questionRequestTag/{userId}/{password}/{questionId}/{tag}")
    public void questionRequestTag(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId,
                                   @PathVariable String tag){
        this.quizService.questionRequestTag(userId, password, questionId, tag);
    }

    /**
     *
     * @param userId
     * @param password
     * @param testId
     * @param questionId
     * Similarly if the user owns this test, directly adds the tag. Otherwise inserts a request regarding adding the tag.
     */
    @PostMapping("/testRequestAddQuestion/{userId}/{password}/{testId}/{questionId}")
    public void testRequestAddQuestion(@PathVariable int userId, @PathVariable String password, @PathVariable int testId,
                                       @PathVariable int questionId){
        this.quizService.testRequestAddQuestion(userId, password, testId, questionId);
    }

    @DeleteMapping("/evaluateQuestionTagReq/{userId}/{password}/{questionId}/{tag}/{approve}")
    public void evaluateQuestionTagReq(@PathVariable int userId, @PathVariable String password, @PathVariable int questionId,
                                       @PathVariable String tag, @PathVariable boolean approve){
        //If approve is true, will add the tag to the question, else will delete all duplicate request
        this.quizService.evaluateQuestionTagReq(userId, password, questionId, tag, approve);
    }

    @DeleteMapping("/evaluateTestAddReq/{userId}/{password}/{testId}/{questionId}/{approve}")
    public void evaluateTestAddReq(@PathVariable int userId, @PathVariable String password, @PathVariable int testId,
                                   @PathVariable int questionId, @PathVariable boolean approve){
        //if approve is true, will add the question to the test, else will delete all duplicate request
        this.quizService.evaluateTestAddReq(userId, password, testId, questionId, approve);
    }

    @GetMapping("/getDifficultyDistributions")
    public List<DifficultyDistributionDto> getDifficultyDistributions(){
        return this.quizService.getDifficultyDistributions();
    }

    @GetMapping("/getQuestionTagDistributions")
    public List<QuestionTagDistributionDto> getQuestionTagDistributions(){
        return this.quizService.getQuestionTagDistributions();
    }
    @GetMapping("/getTestTagDistributions")
    public List<TestTagDistributionDto> getTestTagDistributions(){
        return this.quizService.getTestTagDistributions();
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