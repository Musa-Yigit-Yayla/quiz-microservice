package com.example.quiz_service.repository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
public class UserRepository{
    private JdbcTemplate jdbcTemplate;
    private Connection connection;

    public UserRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) throws SQLException{
        this.jdbcTemplate = jdbcTemplate;
        this.connection = dataSource.getConnection();
    }

    //Adds a user to the user table with automatically generated 8 char length password
    public String addUser(){
        String password = "";
        final int PASSWORD_LENGTH = 8;

        for(int i = 0; i < PASSWORD_LENGTH; i++){
            password += (char)(Math.random() * 128);
        }

        try{
            String query = "INSERT INTO user (password) VALUES (?);";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, password); //1 based indexing
            ps.executeUpdate();
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return password;
    }
    public String getPassword(long id){
        String password = "";

        try{
            String query = "SELECT password FROM user WHERE id = ?;";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                password = rs.getString("password");
            }
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return password;
    }
}