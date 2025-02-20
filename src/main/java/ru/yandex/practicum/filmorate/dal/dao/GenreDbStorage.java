package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage extends BaseDbStorage<Genre> {
    private static final String FIND_ALL_GENRES = "SELECT * FROM genre;";
    private static final String FIND_GENRE_BY_ID = "SELECT * FROM genre WHERE id = ?;";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_GENRES);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        return findOne(FIND_GENRE_BY_ID, genreId);
    }
}
