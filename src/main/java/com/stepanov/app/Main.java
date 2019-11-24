package com.stepanov.app;

import com.stepanov.dao.UsersDao;
import com.stepanov.dao.UsersDaoJdbcTemplateImpl;
import com.stepanov.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://localhost/fix_user_db?serverTimezone=UTC");

        UsersDao usersDao = new UsersDaoJdbcTemplateImpl(dataSource);
        List<User> users = usersDao.findAll();
        System.out.println(users);
    }
}
