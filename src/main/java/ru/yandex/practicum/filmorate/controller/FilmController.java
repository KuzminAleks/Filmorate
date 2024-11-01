package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

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

    @DeleteMapping("/{filmId}")
    public boolean deleteFilm(@PathVariable Integer filmId) {
        return filmService.deleteFilm(filmId);
    }

    @GetMapping
    public List<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{filmId}")
    public FilmDto findFilmById(@PathVariable Integer filmId) {
        return filmService.findFilmById(filmId);
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
}
