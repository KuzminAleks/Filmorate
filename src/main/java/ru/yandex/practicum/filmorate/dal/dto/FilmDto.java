package ru.yandex.practicum.filmorate.dal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FilmDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
}
