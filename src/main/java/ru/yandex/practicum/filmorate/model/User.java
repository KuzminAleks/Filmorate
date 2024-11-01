package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
