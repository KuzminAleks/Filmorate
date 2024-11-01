package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Film;";
    private static final String FIND_FILM_BY_ID = "SELECT * FROM Film WHERE id = ?;";
    private static final String INSERT_QUERY = "INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION) \n" +
            "VALUES (?, ?, ?, ?);";
    private static final String INSERT_FILM_GENRE = "INSERT INTO film_genre VALUES (?, ?);";
    private static final String INSERT_FILM_MPA = "INSERT INTO film_mpa VALUES (?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Film SET name = ?, description = ?, release_date = ?, " +
            "duration = ? WHERE id = ?;";
    private static final String DELETE_FILM_GENRE = "DELETE FROM film_genre WHERE film_id = ?;";
    private static final String DELETE_FILM_MPA = "DELETE FROM film_mpa WHERE film_id = ?;";
    private static final String DELETE_FILM = "DELETE FROM Film WHERE id = ?;";
    private static final String TOP_FILMS = "SELECT f.id," +
            "f.name," +
            "f.description," +
            "f.release_date," +
            "f.duration " +
            "FROM Film AS f " +
            "LEFT JOIN film_likes AS fl ON f.id = fl.film_id " +
            "GROUP BY f.id " +
            "ORDER BY COUNT(fl.user_id) DESC " +
            "LIMIT ?;";
    private static final String ADD_LIKE_FILM = "INSERT INTO film_likes VALUES (?, ?);";
    private static final String REMOVE_LIKE_FILM = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?;";
    private static final String DELETE_FILM_LIKES = "DELETE FROM film_likes WHERE film_id = ?;";
    private static final String FIND_FILM_GENRES = "SELECT g.id, g.name FROM genre g " +
            "LEFT JOIN film_genre fg ON g.id = fg.genre_id " +
            "WHERE fg.film_id = ?;";
    private static final String FIND_FILM_MPA = "SELECT m.id, m.name FROM mpa m " +
            "LEFT JOIN film_mpa fm ON m.id = fm.mpa_id " +
            "WHERE fm.film_id = ?;";

    private final JdbcTemplate jdbc;

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
    }

    public Optional<Film> findFilmById(Integer filmId) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, filmId);

        if (film.isPresent()) {
            film.get().setGenres(new HashSet<>(jdbc.query(FIND_FILM_GENRES, new GenreRowMapper(), filmId)));

            film.get().setMpa(jdbc.queryForObject(FIND_FILM_MPA, new MpaRowMapper(), filmId));
        }

        return film;
    }

    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY).stream()
                .peek(film -> {
                    Integer id = film.getId();

                    try {
                        film.setGenres(new HashSet<>(jdbc.query(FIND_FILM_GENRES, new GenreRowMapper(), id)));
                    } catch (EmptyResultDataAccessException e) {
                        film.setGenres(null);
                    }

                    try {
                        film.setMpa(jdbc.queryForObject(FIND_FILM_MPA, new MpaRowMapper(), id));
                    } catch (EmptyResultDataAccessException e) {
                        film.setMpa(null);
                    }
                })
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        Integer id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration()
        );

        film.setId(id);

        if (film.getGenres() != null) {
            try {
                film.getGenres().forEach(genre -> insert(INSERT_FILM_GENRE, film.getId(), genre.getId()));
            } catch (DataIntegrityViolationException e) {
                throw new ValidationException("Такого жанра не существует.");
            }
        }

        if (film.getMpa() != null) {
            try {
                insert(INSERT_FILM_MPA, film.getId(), film.getMpa().getId());
            } catch (DataIntegrityViolationException e) {
                throw new ValidationException("Такого MPA не существует.");
            }
        }

        return film;
    }

    @Transactional
    public Film updateFilm(Film newFilm) {
        update(UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getId()
        );

        if (newFilm.getGenres() != null) {
            try {
                delete(DELETE_FILM_GENRE, newFilm.getId());
                newFilm.getGenres().forEach(genre -> insert(INSERT_FILM_GENRE, newFilm.getId(), genre.getId()));
            } catch (DataIntegrityViolationException e) {
                throw new ValidationException("Такого жанра не существует.");
            }
        }

        if (newFilm.getMpa() != null) {
            try {
                delete(DELETE_FILM_MPA, newFilm.getId());
                insert(INSERT_FILM_MPA, newFilm.getId(), newFilm.getMpa().getId());
            } catch (DataIntegrityViolationException e) {
                throw new ValidationException("Такого MPA не существует.");
            }
        }

        return newFilm;
    }

    public boolean deleteFilm(Integer filmId) {
        delete(DELETE_FILM_GENRE, filmId);

        delete(DELETE_FILM_LIKES, filmId);

        delete(DELETE_FILM_MPA, filmId);

        return delete(DELETE_FILM, filmId);
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, filmId);

        if (film.isPresent()) {
            insert(ADD_LIKE_FILM, film.get().getId(), userId);

            return film;
        }

        return Optional.empty();
    }

    public Optional<Film> removeLike(Integer filmId, Integer userId) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, filmId);

        if (film.isPresent()) {
            delete(REMOVE_LIKE_FILM, userId, filmId);

            return film;
        }

        return Optional.empty();
    }

    public List<Film> getTopFilms(Integer count) {
        return findMany(TOP_FILMS, count);
    }
}
