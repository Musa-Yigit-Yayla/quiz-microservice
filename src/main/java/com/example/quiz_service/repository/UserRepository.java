package com.example.quiz_service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;


@Repository
public class UserRepository {
    public static final String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private final JdbcTemplate jdbcTemplate;
    private final Connection connection;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
        this.connection = dataSource.getConnection();
    }

    public String addUser(){
        String result = "";
        String password = "";
        long id = -1;
        final int PASSWORD_LENGTH = 8;

        for(int i = 0; i < PASSWORD_LENGTH; i++){
            password += UserRepository.alphanumeric.charAt((int)(Math.random() * alphanumeric.length()));
        }

        try{
            String query = "INSERT INTO user(password) VALUES (?);";
            jdbcTemplate.update(query, password);

            // Now select the largest id of the user table
            query = "SELECT MAX(id) AS max_id FROM user;";
            id = jdbcTemplate.queryForObject(query, Long.class);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            throw ex;
        }

        result = id + ":" + password;
        return result;
    }

    public String getPassword(long id){
        String query = "SELECT password FROM user WHERE id = ?;";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, String.class);
    }

    public String healthCheck() {
        String response = "bad-response";

        try {
            response = "Good response";
            jdbcTemplate.execute("SELECT 1");
        }
        catch(Exception ex){
            response = ex.getMessage();
            return response;
        }
        finally{
            return response;
        }
    }
}
