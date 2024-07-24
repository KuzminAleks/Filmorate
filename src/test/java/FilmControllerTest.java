import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {
    private static FilmController filmController;

    @BeforeEach
    void BeforeEach() {
        filmController = new FilmController();
    }

    @Test
    void shouldNotPassValidationByName() {
        Film someFilm = new Film();
        someFilm.setName("");
        someFilm.setDescription("Трогательный фильм");
        someFilm.setReleaseDate(LocalDate.of(1988, 9, 17));
        someFilm.setDuration(128);

        assertThrows(ValidationException.class, () -> filmController.addFilm(someFilm));

        assertFalse(filmController.getAllFilms().contains(someFilm));
    }

    @Test
    void shouldNotPassValidationByDescription() {
        Film someFilm = new Film();
        someFilm.setName("Человек дождя");
        someFilm.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh " +
                "euismod tincidunt ut laoreet dolore magna aliquam erat volutpat. Ut wisi enim ad minim veniam, " +
                "quis nostrud exerci tatio аbс");
        someFilm.setReleaseDate(LocalDate.of(1988, 9, 17));
        someFilm.setDuration(128);

        assertThrows(ValidationException.class, () -> filmController.addFilm(someFilm));

        assertFalse(filmController.getAllFilms().contains(someFilm));
    }

    @Test
    void shouldNotPassValidationByDateTime() {
        Film someFilm = new Film();
        someFilm.setName("Человек дождя");
        someFilm.setDescription("Трогательный фильм");
        someFilm.setReleaseDate(LocalDate.of(1895, 12, 27));
        someFilm.setDuration(128);

        assertThrows(ValidationException.class, () -> filmController.addFilm(someFilm));

        assertFalse(filmController.getAllFilms().contains(someFilm));
    }

    @Test
    void shouldNotPassValidationByDuration() {
        Film someFilm = new Film();
        someFilm.setName("Человек дождя");
        someFilm.setDescription("Трогательный фильм");
        someFilm.setReleaseDate(LocalDate.of(1988, 9, 17));
        someFilm.setDuration(-128);

        assertThrows(ValidationException.class, () -> filmController.addFilm(someFilm));

        assertFalse(filmController.getAllFilms().contains(someFilm));
    }
}
