package com.example.quiz_service.repository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@Repository
public class QuestionRepository{
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public QuestionRepository(JdbcTemplate jdbcTemplate) throws Exception{
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addQuestion(int userId, String body, String answer0, String answer1,
                                               String answer2, String answer3, int answerIndex){
        String query = "INSERT INTO question(ownerId, body, answer0, answer1, answer2, answer3, answerIndex) " +
                        "VALUES (?, ?, ?, ? ,? ,? ?);";
        this.jdbcTemplate.update(query, userId, body, answer0, answer1, answer2, answer3, answerIndex);
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
}