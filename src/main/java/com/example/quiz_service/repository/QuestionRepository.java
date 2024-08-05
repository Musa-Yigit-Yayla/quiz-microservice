package com.example.quiz_service.repository;
import com.example.quiz_service.Dto.QuestionDto;
import com.example.quiz_service.Dto.QuestionTagRequestDto;
import com.example.quiz_service.Dto.TestAddRequestDto;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


@Repository
public class QuestionRepository{
    private JdbcTemplate jdbcTemplate;
    private Connection connection;

    @Autowired
    public QuestionRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) throws Exception{
        this.jdbcTemplate = jdbcTemplate;
        this.connection = dataSource.getConnection();
    }

    public void addQuestion(int userId, String body, String answer0, String answer1,
                                               String answer2, String answer3, int answerIndex, String difficulty){
        String query = "INSERT INTO question(ownerId, body, answer0, answer1, answer2, answer3, answerIndex, difficulty) " +
                        "VALUES (?, ?, ?, ? ,? ,?, ?, ?);";
        this.jdbcTemplate.update(query, userId, body, answer0, answer1, answer2, answer3, answerIndex, difficulty);
    }

    public void createTest(int userId, String name, String tag){
        String query = "INSERT INTO test(ownerId, name, tag) VALUES(?, ? ,? );";
        this.jdbcTemplate.update(query, userId, name, tag);
    }

    /**
     *
     * @param testId
     * @param questionId
     * attempts and adds the given question to the test, if it is not already present
     */
    public void addQuestionToTest(int testId, int questionId){
        //no need to check whether the test has the question already, as the given parameters comprise the primary key
        String query = "INSERT INTO test_questions(testId, questionId) VALUES (?, ?);";
        this.jdbcTemplate.update(query, testId, questionId);
    }

    /**
     *
     * @param questionId
     * @param tag
     * attempts and adds the given tag to the given question, if that tag is not already present for that question
     */
    public void addQuestionTag(int questionId, String tag){
        String query = "INSERT INTO question_tags(questionId, tag) VALUES (?, ? ,?);";
        this.jdbcTemplate.update(query, questionId, tag);
    }

    /**
     *
     * @param testId
     * @return the id no of the given test's owner
     */
    public int getTestOwnerId(int testId){
        int result = -1;

        String query = "SELECT ownerId FROM question WHERE testId = ?;";

        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, testId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                result = rs.getInt("ownerId");
            }
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}
    }

    /**
     *
     * @param testId
     * @param questionId
     * @param requesterId id of the user who is requesting to add the question into the test
     * attempts and adds the given request, if not already present
     */
    public void testAddRequest(int testId, int questionId, int requesterId){
        String query = "INSERT INTO test_add_request(testId, questionId, requesterId) VALUES (?, ?, ?);";
        this.jdbcTemplate.update(query, testId, questionId, requesterId);
    }

    /**
     *
     * @param questionId
     * @param tag
     * @param requesterId
     * attempts and adds the given request, if not already present
     */
    public void tagAddRequest(int questionId, String tag, int requesterId){
        String query = "INSERT INTO question_tag_request(questionId, tag, requesterId) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(query, questionId, tag, requesterId);
    }

    /**
     *
     * @param userId
     * @param testName
     * @return true if given user has a test with the given name
     */
    public boolean ownsTest(int userId, String testName) {
        String checkQuery = "SELECT * FROM test WHERE ownerId = ? AND name = ?;";
        boolean owns = false;
        try {
            PreparedStatement ps = this.connection.prepareStatement(checkQuery);
            ps.setInt(1, userId);
            ps.setString(2, testName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                owns = true;
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return owns;
        }
        return owns;
    }

    /**
     *
     * @param userId
     * @param questionId
     * @return true if the given user owns the given question
     */
    public boolean ownsQuestion(int userId, int questionId){
        boolean result = false;
        String query = "SELECT * FROM question WHERE ownerId = ? AND questionId = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            
            ps.setInt(1, userId);
            ps.setInt(2, questionId);
            ResultSet rs = ps.executeQuery();
            result = rs.next();
            
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}
    }

    /**
     *
     * @param userId
     * @param testName
     * @return the test add requests for the given test with testname and owner id as user id
     */
    public List<TestAddRequestDto> getTestAddRequests(int userId, String testName){
        int testId = -1;
        List<TestAddRequestDto> result = null;

        String query0 = "SELECT testId from test WHERE ownerId = ? AND name = ?;";

        try {
            PreparedStatement ps = this.connection.prepareStatement(query0);
            ps.setInt(1, userId);
            ps.setString(2, testName);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                testId = rs.getInt("testId");
            }

            if(testId != -1){
                result = new ArrayList<>();

                String query1  = "SELECT * FROM test_add_request WHERE testIs = ?;";
                ps.clearParameters(); //!!! CAREFUL HERE !!!
                ps.setInt(1, testId);
                rs = ps.executeQuery();

                while(rs.next()){
                    TestAddRequestDto tarDTO = new TestAddRequestDto();
                    tarDTO.setTestOwnerId(this.getTestOwnerId(testId));
                    tarDTO.setQuestionId(rs.getInt("questionId"));
                    tarDTO.setRequesterId(rs.getInt("requesterId"));
                    result.add(tarDTO);
                }
            }
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}
    }

    public List<QuestionDto> getSelfQuestions(int userId) {
        List<QuestionDto> result = null;

        String query = "SELECT * FROM question WHERE ownerId = ?;";

        try {
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                QuestionDto qDto = new QuestionDto();
                qDto.setId(rs.getInt("id"));
                qDto.setOwnerId(rs.getInt("ownerId"));
                qDto.setQuestionBody(rs.getString("body"));

                String[] arr = {rs.getString("answer0"), rs.getString("answer1"),
                        rs.getString("answer2"), rs.getString("answer3"),};
                List<String> answers = Arrays.asList(arr);

                qDto.setOptions(answers);
                qDto.setAnswerIndex(rs.getInt("answer_index"));
                qDto.setDifficulty(rs.getString("difficulty"));

                result.add(qDto);
            }
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}

    }

    public List<QuestionTagRequestDto> getQuestionTagRequests(int questionOwnerId, int questionId) {
        List<QuestionTagRequestDto> result = null;

        String query = "SELECT * FROM question_tag_request WHERE questionId = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();

            result = new ArrayList<>();
            while(rs.next()){
                QuestionTagRequestDto qtrDto = new QuestionTagRequestDto();
                qtrDto.setQuestionOwnerId(questionOwnerId);
                qtrDto.setQuestionId(rs.getInt("questionId"));
                qtrDto.setRequesterId(rs.getInt("requesterId"));
                qtrDto.setTag(rs.getString("tag"));

                result.add(qtrDto);
            }
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}
    }

    public void updateQuestion(int questionId, String body, String answer0, String answer1, String answer2,
                               String answer3, int answerIndex, String difficulty) {
        String query = "UPDATE question SET body = ?, answer0 = ?, answer1 = ?, answer2 = ?, answer3 = ?, " +
                "answer_index = ?, difficulty = ? WHERE questionId = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, body);
            ps.setString(2, answer0);
            ps.setString(3, answer1);
            ps.setString(4, answer2);
            ps.setString(5, answer3);
            ps.setInt(6, answerIndex);
            ps.setString(7, difficulty);
            ps.setInt(8, questionId);

            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }

    public void deleteQuestion(int questionId) {
        String query = "DELETE FROM question WHERE id = ?;";
        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }

    public void updateTest(int userId, String currName, String newName, String tag) {
        String query = "UPDATE test SET name = ?, tag = ? WHERE ownerId = ? AND name = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setString(1, newName);
            ps.setString(2, tag);
            ps.setInt(3, userId);
            ps.setString(4, currName);

            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }

    /**
     *
     * @param userId ownerId
     * @param name testName
     * deletes the test if the given user owns a test with the given name
     */
    public void deleteTest(int userId, String name) {
        String query = "DELETE FROM test WHERE ownerId = ? AND name = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, name);

            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }
}