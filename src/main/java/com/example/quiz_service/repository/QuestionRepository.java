package com.example.quiz_service.repository;
import com.example.quiz_service.Dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.slf4j.Slf4j; //for printing debug statements

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
    public static final Logger logger = LoggerFactory.getLogger(QuestionRepository.class);

    @Autowired
    public QuestionRepository(JdbcTemplate jdbcTemplate, DataSource dataSource){
        this.jdbcTemplate = jdbcTemplate;

        try {
            this.connection = dataSource.getConnection();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void addQuestion(int userId, String body, String answer0, String answer1,
                                               String answer2, String answer3, int answerIndex, String difficulty){
        String query = "INSERT INTO question(ownerId, body, answer0, answer1, answer2, answer3, answer_index, difficulty) " +
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
        String query = "INSERT INTO question_tags(questionId, tag) VALUES (?, ?);";
        this.jdbcTemplate.update(query, questionId, tag);
    }

    /**
     *
     * @param testId
     * @return the id no of the given test's owner
     */
    public int getTestOwnerId(int testId){
        int result = -1;

        String query = "SELECT ownerId FROM test WHERE id = ?;";

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
     * attempts and adds the given request, if not already present in the request table
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
     * attempts and adds the given request, if not already present in the request table
     */
    public void tagAddRequest(int questionId, String tag, int requesterId){
        String query = "INSERT INTO question_tag_request(questionId, tag, requesterId) VALUES (?, ?, ?)";
        this.jdbcTemplate.update(query, questionId, tag, requesterId);
    }

    /**
     *
     * @param questionId
     * @param tag
     * Rejects all the tag requests with the given tag, for the given question
     */
    public void rejectTagAddRequests(int questionId, String tag){
        String query = "DELETE FROM question_tag_request WHERE questionId = ? AND tag = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, questionId);
            ps.setString(2, tag);
            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }

    /**
     *
     * @param questionId
     * @param testId
     * rejects all the test add requests with the given question and test
     */
    public void rejectTestAddRequests(int questionId, int testId) {
        String query = "DELETE FROM test_add_request WHERE testId = ? AND questionId = ?;";

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, testId);
            ps.setInt(2, questionId);
            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
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

            owns = rs.next();
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
        String query = "SELECT * FROM question WHERE ownerId = ? AND id = ?;";

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

        String query0 = "SELECT id from test WHERE ownerId = ? AND name = ?;";

        try {
            PreparedStatement ps = this.connection.prepareStatement(query0);
            ps.setInt(1, userId);
            ps.setString(2, testName);

            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                testId = rs.getInt("id");
            }

            if(testId != -1){
                result = new ArrayList<>();

                String query1  = "SELECT * FROM test_add_request AS tar JOIN test ON (tar.testId = test.id) WHERE tar.testId = ?;";
                ps = this.connection.prepareStatement(query1);
                ps.setInt(1, testId);
                rs = ps.executeQuery();

                while(rs.next()){
                    TestAddRequestDto tarDTO = new TestAddRequestDto();
                    tarDTO.setTestOwnerId(this.getTestOwnerId(testId));
                    tarDTO.setQuestionId(rs.getInt("questionId"));
                    tarDTO.setRequesterId(rs.getInt("requesterId"));
                    tarDTO.setTestId(rs.getInt("id"));
                    tarDTO.setTestName(rs.getString("name"));
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

            result = new ArrayList<>();
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
                "answer_index = ?, difficulty = ? WHERE id = ?;";

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

            //logger.info("Debug: about to executeUpdate on QuestionRepository.deleteTest");
            ps.executeUpdate();
        }
        catch(Exception e){ System.out.println(e.getMessage());}
    }

    public List<DifficultyDistributionDto> getDifficultyDistributions() {
        String query = "SELECT * FROM difficulty_distributions;";

        List<DifficultyDistributionDto> result = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            result = new ArrayList<>();

            while(rs.next()){
                DifficultyDistributionDto dto = new DifficultyDistributionDto();

                dto.setQuestionCount(rs.getInt("question_count"));
                dto.setDifficulty(rs.getString("difficulty"));
                result.add(dto);
            }
            return result;
        }
        catch(Exception e){ System.out.println(e.getMessage()); return result;}
    }

    public List<QuestionTagDistributionDto> getQuestionTagDistributions() {
        String query = "SELECT * FROM question_tag_distributions;";

        List<QuestionTagDistributionDto> result = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            result = new ArrayList<>();

            while(rs.next()){
                QuestionTagDistributionDto dto = new QuestionTagDistributionDto();
                dto.setQuestionCount(rs.getInt("question_count"));
                dto.setTag(rs.getString("tag"));

                result.add(dto);
            }
        }
        catch(Exception e){ System.out.println(e.getMessage());}
        finally{
            return result;
        }
    }

    public List<TestTagDistributionDto> getTestTagDistributions() {
        String query = "SELECT * FROM test_tag_distributions;";
        List<TestTagDistributionDto> result = null;

        try{
            ResultSet rs = this.connection.prepareStatement(query).executeQuery();
            result = new ArrayList<>();

            while(rs.next()){
                TestTagDistributionDto dto = new TestTagDistributionDto();
                dto.setTestCount(rs.getInt("test_count"));
                dto.setTag(rs.getString("tag"));

                result.add(dto);
            }
        }
        catch(Exception e){ System.out.println(e.getMessage());}
        finally{
            return result;
        }
    }

    public QuestionTagsDto getQuestionTags(int questionId) {
        String query = "SELECT tag FROM question_tags WHERE questionId = ?;";
        QuestionTagsDto result = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, questionId);

            ResultSet rs = ps.executeQuery();
            result = new QuestionTagsDto();
            result.setQuestionId(questionId);
            List<String> tagList = new ArrayList<>();

            while(rs.next()){
                tagList.add(rs.getString("tag"));
            }
            result.setTags(tagList);
        }
        catch(Exception e){ System.out.println(e.getMessage());}
        finally{
            return result;
        }
    }

    public List<TestAddRequestDto> getSentTestAddRequests(int userId) {
        String query = "SELECT * FROM test_add_request AS tar JOIN test ON(tar.testId = test.id) WHERE requesterId = ?;";
        List<TestAddRequestDto> list = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();

            while(rs.next()){
                TestAddRequestDto dto = new TestAddRequestDto();

                dto.setRequesterId(rs.getInt("requesterId"));
                dto.setQuestionId(rs.getInt("questionId"));
                dto.setTestId(rs.getInt("testId"));
                dto.setTestName(rs.getString("name"));
                dto.setTestOwnerId(rs.getInt("ownerId"));
                
                list.add(dto);
            }
        }
        catch(Exception e){ System.out.println(e.getMessage());}
        finally{
            return list;
        }
    }

    public List<QuestionTagRequestDto> getSentQuestionTagRequests(int userId) {
        String query = "SELECT * FROM question JOIN question_tag_request ON (id = questionId) WHERE requesterId = ?;";
        List<QuestionTagRequestDto> list = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            list = new ArrayList<>();

            while(rs.next()){
                QuestionTagRequestDto dto = new QuestionTagRequestDto();
                dto.setQuestionId(rs.getInt("questionId"));
                dto.setRequesterId(rs.getInt("requesterId"));
                dto.setTag(rs.getString("tag"));
                dto.setQuestionOwnerId(rs.getInt("ownerId"));

                list.add(dto);
            }
        }
        catch(Exception e){ System.out.println(e.getMessage());}
        finally{
            return list;
        }
    }

    /**
     *
     * @param tag
     * @param difficulty
     * @return a list of questions where they have the given difficulty and one of their tags have a string match
     * with the given tag (given tag can be a substring of one of the question's tags)
     */
    public List<QuestionDto> filterQuestion(String tag, String difficulty) {
        String query = "WITH filtered_question AS " +
                "(SELECT * FROM question WHERE EXISTS (SELECT tag FROM question_tags WHERE questionId = question.id AND tag LIKE ?)) " +
                "SELECT * FROM filtered_question WHERE difficulty = ? ORDER BY id ASC;";

        List<QuestionDto> result = null;
        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            String tagRegex = "%" + tag + "%";
            ps.setString(1, tagRegex);
            ps.setString(2, difficulty);
            ResultSet rs = ps.executeQuery();
            result = new ArrayList<>();

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
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            return result;
        }
    }

    public List<TestDto> getTests() {
        String query = "SELECT * FROM test";
        List<TestDto> result = null;

        try{
            PreparedStatement ps = this.connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            result = new ArrayList<>();

            while(rs.next()){
                TestDto dto = new TestDto();
                dto.setId(rs.getInt("id"));
                dto.setOwnerId(rs.getInt("ownerId"));
                dto.setName(rs.getString("name"));
                dto.setTag(rs.getString("tag"));

                result.add(dto);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            return result;
        }
    }

    public TestWithQuestionsDto getTest(int testId) {
        String query0 = "SELECT * FROM test WHERE id = ?;";
        String query1 = "SELECT * FROM question WHERE id IN (SELECT questionId FROM test_questions WHERE testId = ?);";

        TestWithQuestionsDto dto = null;

        try{
            PreparedStatement ps0 = this.connection.prepareStatement(query0);
            PreparedStatement ps1 = this.connection.prepareStatement(query1);

            ps0.setInt(1, testId);
            ps1.setInt(1, testId);

            ResultSet rs0 = ps0.executeQuery();
            ResultSet rs1 = ps1.executeQuery();

            dto = new TestWithQuestionsDto();
            TestDto testDto = new TestDto();
            ArrayList<QuestionDto> questions = new ArrayList<>();

            if(rs0.next()){
                testDto.setId(rs0.getInt("id"));
                testDto.setOwnerId(rs0.getInt("ownerId"));
                testDto.setName(rs0.getString("name"));
                testDto.setTag(rs0.getString("tag"));
            }

            while(rs1.next()){
                QuestionDto qDto = new QuestionDto();

                qDto.setId(rs1.getInt("id"));
                qDto.setOwnerId(rs1.getInt("ownerId"));
                qDto.setQuestionBody(rs1.getString("body"));

                String[] arr = {rs1.getString("answer0"), rs1.getString("answer1"),
                        rs1.getString("answer2"), rs1.getString("answer3"),};
                List<String> answers = Arrays.asList(arr);

                qDto.setOptions(answers);
                qDto.setAnswerIndex(rs1.getInt("answer_index"));
                qDto.setDifficulty(rs1.getString("difficulty"));

                questions.add(qDto);
            }
            dto.setTestDto(testDto);
            dto.setQuestions(questions);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            return dto;
        }
    }
}