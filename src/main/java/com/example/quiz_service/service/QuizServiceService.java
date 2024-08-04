package com.example.quiz_service.service;
import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.Dto.TestAddRequestDto;
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
                            String answer3, int answerIndex){
        //validate user password in service level and if matches add the question via repository's method
        String pw = this.userRepository.getPassword(userId);
        if(pw.equals(password)){
            this.questionRepository.addQuestion(userId, body, answer0, answer1, answer2, answer3, answerIndex);
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


        }



    }
}