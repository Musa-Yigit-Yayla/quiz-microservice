package com.example.quiz_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class DataConfig {

    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://mysql-service:3307/mydb");
        dataSource.setUsername("user");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void printDataSourceInfo() {
        DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
        if (dataSource instanceof DriverManagerDataSource) {
            DriverManagerDataSource driverManagerDataSource = (DriverManagerDataSource) dataSource;
            System.out.println("DataSource URL: " + driverManagerDataSource.getUrl());
            System.out.println("DataSource Username: " + driverManagerDataSource.getUsername());
        }
        else {
            System.out.println("DataSource is not of type DriverManagerDataSource");
        }
    }
}
