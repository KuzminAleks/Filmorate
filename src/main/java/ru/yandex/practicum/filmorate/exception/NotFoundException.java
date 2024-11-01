package ru.yandex.practicum.filmorate.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String mes) {
        super(mes);

        Logger log = LoggerFactory.getLogger(FilmController.class);
        log.warn(mes);
    }
}
