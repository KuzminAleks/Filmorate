package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dal.mapper.FilmMapper.mapToFilmDto;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmDbStorage filmDbStorage;

    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public List<FilmDto> getAllFilms() {
        return filmDbStorage.getAllFilms().stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    public FilmDto findFilmById(Integer filmId) {
        Optional<Film> film = filmDbStorage.findFilmById(filmId);

        System.out.println(film.map(FilmMapper::mapToFilmDto));

        return film.map(FilmMapper::mapToFilmDto).orElse(null);
    }

    public FilmDto addFilm(Film film) {
        log.debug(film.toString());

        if (isValidated(film)) {
            filmDbStorage.addFilm(film);

            log.debug(film.toString());

            return mapToFilmDto(film);
        }

        log.debug(film.toString());

        throw new InternalServerException("Фильм не добавлен, что-то пошло не так.");
    }

    public FilmDto updateFilm(Film film) {
        if (isValidated(film)) {
            log.debug(film.toString());

            return mapToFilmDto(filmDbStorage.updateFilm(film));
        }

        log.debug(film.toString());

        throw new InternalServerException("Фильм не обновлен, что-то пошло не так.");
    }

    public boolean deleteFilm(Integer filmId) {
        return filmDbStorage.deleteFilm(filmId);
    }

    public FilmDto addLike(Integer filmId, Integer userId) {
        Optional<Film> film = filmDbStorage.addLike(filmId, userId);

        log.debug(film.toString());

        if (film.isPresent()) {
            return mapToFilmDto(film.get());
        } else {
            throw new InternalServerException("Лайк не был добавлен, что-то пошло не так.");
        }
    }

    public FilmDto removeLike(Integer filmId, Integer userId) {
        Optional<Film> film = filmDbStorage.removeLike(filmId, userId);

        log.debug(film.toString());

        if (film.isPresent()) {
            return mapToFilmDto(film.get());
        } else {
            throw new InternalServerException("Лайк не был удален, что-то пошло не так.");
        }
    }

    public List<FilmDto> getTopFilms(Integer count) {
        return filmDbStorage.getTopFilms(count).stream()
                .map(FilmMapper::mapToFilmDto)
                .collect(Collectors.toList());
    }

    private boolean isValidated(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым!");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть до 28 декабря 1895 года.");
        }

        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной!");
        }

        return true;
    }
}
