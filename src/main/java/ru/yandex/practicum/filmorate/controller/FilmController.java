package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dal.dao.FilmDbStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    private final Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public FilmDto addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public FilmDto updateFilm(@RequestBody Film newFilm) {
        return filmService.updateFilm(newFilm);
    }

    @GetMapping
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping("{filmId}/like/{userId}")
    public FilmDto addLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public FilmDto removeLike(@PathVariable Integer filmId, @PathVariable Integer userId) {
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getTopFilms(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

//    @GetMapping("{filmId}/likes")
//    public Set<UserDto> getLikesFilm(@PathVariable Integer filmId) {
//        return filmService.getLikesFilm(filmId);
//    }
}
