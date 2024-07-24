package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Film {
    Integer id;
    String name;
    String description;
    LocalDate releaseDate;
    Integer duration;
}
