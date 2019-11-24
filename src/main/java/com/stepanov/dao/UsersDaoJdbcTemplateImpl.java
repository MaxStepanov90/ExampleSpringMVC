package com.stepanov.dao;

import com.stepanov.models.Car;
import com.stepanov.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private Map<Long, User> usersMap = new HashMap<>();

    //     language=SQL
    private final String SQL_SELECT_uSER_WITH_CARS =
            "SELECT fix_user.*, fix_car.id as car_id, fix_car.model FROM fix_user LEFT JOIN fix_car" +
                    "ON fix_user.id = fix.car.owner_id WHERE fix_user.id = ?";
    //    language=SQL
    private final String SQL_SELECT_USERS_WITH_CARS =
            "SELECT fix_user.*, fix_car.id as car_id, fix_car.model FROM fix_user LEFT JOIN fix_car" +
                    " ON fix_user.id = fix_car.owner_id";
    //     language=SQL
    private final String SQL_SELECT_ALL_BY_FIRST_NAME =
            "SELECT * FROM fix_user WHERE first_name = ?";
    //    language=SQL
    private final String SQL_SELECT_USER_BY_ID =
            "SELECT*FROM fix_user WHERE id = :id";
    //    language=SQL
    private final String SQL_INSERT_USER =
            "INSERT INTO fix_user (first_name, last_name) VALUES (:firstName, :lastName )";

    @Autowired
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }
    private RowMapper<User> userRowMapperWithoutCars = (resultSet, i) -> User.builder()
            .id(resultSet.getLong("id"))
            .firstName(resultSet.getString("first_name"))
            .lastName(resultSet.getString("last_name"))
            .build();

    private RowMapper<User> userRowMapper
            = (ResultSet resultSet, int i) -> {
        Long id = resultSet.getLong("id");

        if (!usersMap.containsKey(id)) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            User user = new User(id, firstName, lastName, new ArrayList<>());
            usersMap.put(id, user);
        }
        Car car = new Car(resultSet.getLong("car_id"),
                resultSet.getString("model"), usersMap.get(id));

        usersMap.get(id).getCars().add(car);

        return usersMap.get(id);
    };


    @Override
    public List<User> findAllByFirstName(String firstName) {
        return jdbcTemplate.query(SQL_SELECT_ALL_BY_FIRST_NAME, userRowMapperWithoutCars, firstName);
    }

    @Override
    public Optional<User> find(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<User> result = namedParameterJdbcTemplate.query(SQL_SELECT_USER_BY_ID, params, userRowMapperWithoutCars);
        if (result.isEmpty()){
           return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void save(User model) {
        Map<String,Object>params = new HashMap<>();
        params.put("firstName", model.getFirstName());
        params.put("lastName",model.getLastName());
        namedParameterJdbcTemplate.update(SQL_INSERT_USER,params);
    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_SELECT_USERS_WITH_CARS, userRowMapper);
    }
}

