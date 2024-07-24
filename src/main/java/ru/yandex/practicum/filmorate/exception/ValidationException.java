package ru.yandex.practicum.filmorate.exception;
import ru.yandex.practicum.filmorate.controller.FilmController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidationException extends RuntimeException {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    public ValidationException(String mes) {
        super(mes);

        log.warn("Ошибка валидации: {}", mes);
    }
}
