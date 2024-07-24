package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (isValidated(film)) {
            film.setId(getNextId());

            films.put(film.getId(), film);

            log.info("Фильм \"{}\" успешно добавлен.", film.getName());
        }

        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (isValidated(newFilm)) {
            if (newFilm.getId() == null) {
                throw new ValidationException("Id должен быть указан");
            }

            if (films.containsKey(newFilm.getId())) {
                Film oldFilm = films.get(newFilm.getId());

                oldFilm.setName(newFilm.getName());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                oldFilm.setDuration(newFilm.getDuration());

                log.info("Фильм \"{}\" успешно изменен.", oldFilm.getName());

                return oldFilm;
            }
        }

        log.info("Фильм не найден.");

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Фильм с id = " + newFilm.getId() + " не найден.");
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Вывод всех фильмов.");

        return films.values();
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

    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);

        log.debug("Максимальное используемое id: {}", currentMaxId);
        log.debug("Доступное максимальное id: {}", currentMaxId + 1);

        return ++currentMaxId;
    }
}
