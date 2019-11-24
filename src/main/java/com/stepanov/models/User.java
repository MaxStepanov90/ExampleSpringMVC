package com.stepanov.models;

import com.stepanov.forms.UserForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

// аннотации автоматически генерируют сеттеры, геттеры, конструкторы и паттерн Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class User {

    private Long id;
    private String firstName;
    private String lastName;

    private List<Car> cars;

    public static User from(UserForm form){
        return User.builder()
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .build();
    }
}
