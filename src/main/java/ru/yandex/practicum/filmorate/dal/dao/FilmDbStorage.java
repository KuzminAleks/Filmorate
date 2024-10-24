package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmDbStorage extends BaseDbStorage<Film> {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Film";
    private static final String FIND_FILM_BY_ID = "SELECT * FROM Film WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO Film (NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA) \n" +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String UPDATE_QUERY = "UPDATE Film SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, mpa = ? WHERE id = ?";
    private static final String TOP_FILMS = "SELECT f.name\n" +
            "FROM Film AS f\n" +
            "LEFT JOIN film_likes AS fl ON f.id = fl.film_id\n" +
            "GROUP BY f.name\n" +
            "ORDER BY COUNT(fl.user_id) DESC\n" +
            "LIMIT ?";
    private static final String GET_USER_LIKE_FILM = "SELECT u.login FROM \"User\" AS u\n" +
            "RIGHT JOIN FILM_LIKES AS fl ON u.id = fl.user_id\n" +
            "WHERE fl.FILM_ID = ?;";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Film> findFilmById(Integer filmId) {
        return findOne(FIND_FILM_BY_ID, filmId);
    }

    public List<Film> getAllFilms() {
        return findMany(FIND_ALL_QUERY);
    }

    public Film updateFilm(Film newFilm) {
        update(UPDATE_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa(),
                newFilm.getId()
        );

        return newFilm;
    }

    public Film addFilm(Film film) {
        Integer id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa()
        );

        film.setId(id);

        return film;
    }

    public Optional<Film> addLike(Integer filmId, Integer userId) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, filmId);

        if (film.isPresent()) {
            //film.get().getLikes().add(userId);

            insert("INSERT INTO film_likes VALUES (?, ?);", film.get().getId(), userId);

            return film;
        }

        return Optional.empty();
    }

    public Optional<Film> removeLike(Integer filmId, Integer userId) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, filmId);

        if (film.isPresent()) {
            //film.get().getLikes().add(userId);

            delete("DELETE FROM film_likes WHERE user_id = ?;", userId);

            return film;
        }

        return Optional.empty();
    }

    public List<Film> getTopFilms(Integer count) {
        return findMany(TOP_FILMS, count);
    }

//    public List<UserDto> getLikesFilm(Integer filmId) {
//        return findMany(GET_USER_LIKE_FILM, filmId);
//    }
}
