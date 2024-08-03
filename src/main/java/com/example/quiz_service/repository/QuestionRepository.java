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
}