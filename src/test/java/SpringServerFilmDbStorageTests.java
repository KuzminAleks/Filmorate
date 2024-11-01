import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.SpringServer;
import ru.yandex.practicum.filmorate.dal.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {SpringServer.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmRowMapper.class})
public class SpringServerFilmDbStorageTests {
    private final FilmDbStorage filmStorage;

    @Test
    public void testGetFilmById() {
        Optional<Film> filmOptional = filmStorage.findFilmById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> assertThat(film)
                        .hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .hasSize(4)
                .extracting(Film::getName)
                .containsExactlyInAnyOrder("Test1", "Test2", "Test3", "Test4");
    }

    @Test
    public void testAddFilm() {
        Film film = new Film();

        film.setName("insertTest");
        film.setDescription("Some desc");
        film.setReleaseDate(LocalDate.of(1997, 9, 15));
        film.setDuration(100);

        Genre genre = new Genre();

        genre.setId(1);
        genre.setName("Комедия");

        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        film.setGenres(genres);

        Mpa mpa = new Mpa();

        mpa.setId(2);
        mpa.setName("PG");

        film.setMpa(mpa);

        filmStorage.addFilm(film);

        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .extracting(Film::getName)
                .contains("insertTest");
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film();

        film.setName("insertTest");
        film.setDescription("Some desc");
        film.setReleaseDate(LocalDate.of(1997, 9, 15));
        film.setDuration(100);

        Genre genre = new Genre();

        genre.setId(1);
        genre.setName("Комедия");

        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        film.setGenres(genres);

        Mpa mpa = new Mpa();

        mpa.setId(2);
        mpa.setName("PG");

        film.setMpa(mpa);

        filmStorage.addFilm(film);

        film.setName("updateTest");

        filmStorage.updateFilm(film);

        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .extracting(Film::getName)
                .contains("updateTest");
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film();

        film.setName("insertTest");
        film.setDescription("Some desc");
        film.setReleaseDate(LocalDate.of(1997, 9, 15));
        film.setDuration(100);

        Genre genre = new Genre();

        genre.setId(1);
        genre.setName("Комедия");

        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        film.setGenres(genres);

        Mpa mpa = new Mpa();

        mpa.setId(2);
        mpa.setName("PG");

        film.setMpa(mpa);

        filmStorage.addFilm(film);

        filmStorage.deleteFilm(film.getId());

        List<Film> films = filmStorage.getAllFilms();

        assertThat(films)
                .extracting(Film::getName)
                .doesNotContain("updateTest");
    }

    @Test
    public void testAddLike() {
        Film film = new Film();

        film.setName("insertTest");
        film.setDescription("Some desc");
        film.setReleaseDate(LocalDate.of(1997, 9, 15));
        film.setDuration(100);

        Genre genre = new Genre();

        genre.setId(1);
        genre.setName("Комедия");

        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        film.setGenres(genres);

        Mpa mpa = new Mpa();

        mpa.setId(2);
        mpa.setName("PG");

        film.setMpa(mpa);

        filmStorage.addFilm(film);

        Integer userId = 1;

        filmStorage.addLike(film.getId(), userId);

        List<Film> films = filmStorage.getTopFilms(10);

        assertThat(films)
                .extracting(Film::getId)
                .contains(film.getId());

        filmStorage.removeLike(film.getId(), userId);
    }

    @Test
    public void testRemoveLike() {
        Film film = new Film();

        film.setName("Test");
        film.setDescription("Some desc");
        film.setReleaseDate(LocalDate.of(1997, 9, 15));
        film.setDuration(100);

        Genre genre = new Genre();

        genre.setId(1);
        genre.setName("Комедия");

        Set<Genre> genres = new HashSet<>();
        genres.add(genre);

        film.setGenres(genres);

        Mpa mpa = new Mpa();

        mpa.setId(2);
        mpa.setName("PG");

        film.setMpa(mpa);

        filmStorage.addFilm(film);

        Integer userId = 2;

        filmStorage.addLike(film.getId(), userId);

        List<Film> films = filmStorage.getTopFilms(10);

        assertThat(films)
                .extracting(Film::getId)
                .contains(film.getId());

        filmStorage.removeLike(film.getId(), userId);

        films = filmStorage.getTopFilms(1);

        assertThat(films)
                .extracting(Film::getId)
                .doesNotContain(film.getId());
    }

    @Test
    public void testGetTopFilms() {
        List<Film> films = filmStorage.getTopFilms(10);

        assertThat(films)
                .hasSize(4)
                .extracting(Film::getName)
                .containsExactlyInAnyOrder("Test1", "Test2", "Test3", "Test4");
    }
}
