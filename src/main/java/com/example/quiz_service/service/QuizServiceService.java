package com.example.quiz_service.service;
import com.example.quiz_service.Dto.*;
import com.example.quiz_service.repository.QuestionRepository;
import com.example.quiz_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.lang.Exception;
import java.util.List;

@Service
public class QuizServiceService{
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public QuizServiceService(UserRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
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

    public void addQuestion(int userId, String password, String body, String answer0, String answer1, String answer2,
                            String answer3, int answerIndex, String difficulty){
        //validate user password in service level and if matches add the question via repository's method
        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password)){
            this.questionRepository.addQuestion(userId, body, answer0, answer1, answer2, answer3, answerIndex, difficulty);
        }
    }
    public void createTest(int userId, String password, String name, String tag){
        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password)){
            this.questionRepository.createTest(userId, name, tag);
        }
    }

    public String healthCheck() {
        return this.userRepository.healthCheck();
    }

    /**
     *
     * @param userId
     * @param password
     * @param testName
     * @return the add requests registered for the test with owner id as userId, and given test name
     */
    public List<TestAddRequestDto> getTestAddRequests(int userId, String password, String testName) {
        List<TestAddRequestDto> result = null;

        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password) && this.questionRepository.ownsTest(userId, testName)){
            result = this.questionRepository.getTestAddRequests(userId, testName);
        }
        return result;
    }

    public List<QuestionDto> getSelfQuestions(int userId, String password) {
        List<QuestionDto> result = null;

        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password)){
            result = this.questionRepository.getSelfQuestions(userId);
        }
        return result;
    }

    public List<QuestionTagRequestDto> getQuestionTagRequests(int userId, String password, int questionId){
        List<QuestionTagRequestDto> result = null;

        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password)){
            result = this.questionRepository.getQuestionTagRequests(userId, questionId);
        }
        return result;
    }

    public void updateQuestion(int userId, String password, int questionId, String body, String answer0, String answer1,
                               String answer2, String answer3, int answerIndex, String difficulty) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password) && this.questionRepository.ownsQuestion(userId, questionId)){
            this.questionRepository.updateQuestion(questionId, body, answer0, answer1, answer2, answer3, answerIndex, difficulty);
        }
    }

    public void deleteQuestion(int userId, String password, int questionId) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password) && this.questionRepository.ownsQuestion(userId, questionId)){
            this.questionRepository.deleteQuestion(questionId);
        }

    }

    /**
     *
     * @param userId
     * @param password
     * @param currName is current testName
     * @param newName is new testName
     * @param tag
     */
    public void updateTest(int userId, String password, String currName, String newName, String tag) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password) && this.questionRepository.ownsTest(userId, currName)){
            this.questionRepository.updateTest(userId, currName, newName, tag);
        }
    }

    /**
     *
     * @param userId
     * @param password
     * @param name test name
     */
    public void deleteTest(int userId, String password, String name){
        String pw = this.userRepository.getPassword(userId);

        //QuestionRepository.logger.info("Debug: about to check if condition in quizService.deleteTest, ownsTest yields " +
        //        this.questionRepository.ownsTest(userId, name));
        if(pw.equals(password) && this.questionRepository.ownsTest(userId, name)){
            //QuestionRepository.logger.info("Debug: if condition in quizService.deleteTest yields true");
            this.questionRepository.deleteTest(userId, name);
        }
    }

    public List<DifficultyDistributionDto> getDifficultyDistributions() {
        return this.questionRepository.getDifficultyDistributions();
    }

    public List<QuestionTagDistributionDto> getQuestionTagDistributions() {
        return this.questionRepository.getQuestionTagDistributions();
    }

    public List<TestTagDistributionDto> getTestTagDistributions() {
        return this.questionRepository.getTestTagDistributions();
    }

    public void questionRequestTag(int userId, String password, int questionId, String tag) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password)){
            //check whether if the user owns the question
            if(this.questionRepository.ownsQuestion(userId, questionId)){
                this.questionRepository.addQuestionTag(questionId, tag); //add directly
            }
            else{
                this.questionRepository.tagAddRequest(questionId, tag, userId); //add the tag add request
            }
        }
    }

    /**
     *
     * @param userId
     * @param password
     * @param testId
     * @param questionId
     * if the user is test owner, add the question directly, else create a request
     */
    public void testRequestAddQuestion(int userId, String password, int testId, int questionId) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password)){
            int testOwnerId = this.questionRepository.getTestOwnerId(testId);

            if(userId == testOwnerId){
                //add directly
                this.questionRepository.addQuestionToTest(testId, questionId);
            }
            else{
                this.questionRepository.testAddRequest(testId, questionId, userId);
            }
        }
    }

    public void evaluateQuestionTagReq(int userId, String password, int questionId, String tag, boolean approve) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password) && this.questionRepository.ownsQuestion(userId, questionId)){
            if(approve){
                this.questionRepository.addQuestionTag(questionId, tag); //this will automatically have trigger invoked to release
                //previous tag requests
            }
            else{
                this.questionRepository.rejectTagAddRequests(questionId, tag);
            }
        }
    }

    public void evaluateTestAddReq(int userId, String password, int testId, int questionId, boolean approve) {
        String pw = this.userRepository.getPassword(userId);

        if(pw.equals(password) && this.questionRepository.getTestOwnerId(testId) == userId){
            if(approve){
                this.questionRepository.addQuestionToTest(testId, questionId); //this will have trigger executed
            }
            else{
                this.questionRepository.rejectTestAddRequests(questionId, testId);
            }
        }
    }

    /**
     *
     * @param questionId
     * @return the tags given question has
     */
    public QuestionTagsDto getQuestionTags(int questionId) {
        return this.questionRepository.getQuestionTags(questionId);
    }

    public List<TestAddRequestDto> getSentTestAddRequests(int userId, String password) {
        String pw = this.userRepository.getPassword(userId);
        List<TestAddRequestDto> result = null;

        if(password.equals(pw)){
            result = this.questionRepository.getSentTestAddRequests(userId);
        }
        return result;
    }

    public List<QuestionTagRequestDto> getSentQuestionTagRequests(int userId, String password) {
        String pw = this.userRepository.getPassword(userId);
        List<QuestionTagRequestDto> result = null;

        if(password.equals(pw)){
            result = this.questionRepository.getSentQuestionTagRequests(userId);
        }
        return result;
    }
}