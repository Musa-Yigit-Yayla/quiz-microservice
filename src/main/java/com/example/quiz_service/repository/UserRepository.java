package com.example.quiz_service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) throws Exception {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String addUser(){
        String result = "";
        String password = "";
        long id = -1;
        final int PASSWORD_LENGTH = 8;

        for(int i = 0; i < PASSWORD_LENGTH; i++){
            password += (char) (Math.random() * 128);
        }

        try{
            String query = "INSERT INTO user (password) VALUES (?);";
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
