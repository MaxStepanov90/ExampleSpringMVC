package com.stepanov.models;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "owner")

public class Car {

    private long id;
    private String model;

    private User owner;

}
