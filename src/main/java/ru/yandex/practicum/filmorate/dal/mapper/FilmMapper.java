package ru.yandex.practicum.filmorate.dal.mapper;

import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;

public final class FilmMapper {
    public static FilmDto mapToFilmDto(Film film) {
        FilmDto dto = new FilmDto();

        dto.setId(film.getId());
        dto.setName(film.getName());
        dto.setDescription(film.getDescription());
        dto.setReleaseDate(film.getReleaseDate());
        dto.setDuration(film.getDuration());

        return dto;
    }

    public static Film mapToFilm(FilmDto filmDto) {
        Film film = new Film();

        film.setId(filmDto.getId());
        film.setName(filmDto.getName());
        film.setDescription(filmDto.getDescription());
        film.setReleaseDate(filmDto.getReleaseDate());
        film.setDuration(filmDto.getDuration());

        return film;
    }
}
