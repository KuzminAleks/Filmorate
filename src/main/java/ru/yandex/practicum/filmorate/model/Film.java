package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
