package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.GenreDto;
import ru.yandex.practicum.filmorate.dal.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final GenreDbStorage genreDbStorage;

    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<GenreDto> getAllGenres() {
        return genreDbStorage.getAllGenres().stream()
                .map(GenreMapper::mapToGenreDto)
                .collect(Collectors.toList());
    }

    public GenreDto getGenresById(Integer genreId) {
        Optional<Genre> genre = genreDbStorage.getGenreById(genreId);
        return genre.map(GenreMapper::mapToGenreDto).orElseThrow(() -> new NotFoundException("Такого жанра не существует."));
    }
}
