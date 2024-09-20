package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    public User addUser(User user) {
        if (isValidated(user)) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            user.setId(getNextId());

            user.setFriends(new HashSet<>());

            users.put(user.getId(), user);

            log.info("Пользователь \"{}\" успешно добавлен.", user.getName());
        }

        return user;
    }

    public User updateUser(User newUser) {
        if (isValidated(newUser)) {
            if (newUser.getId() == null) {
                throw new ValidationException("Id должен быть указан");
            }

            if (users.containsKey(newUser.getId())) {
                User oldUser = users.get(newUser.getId());

                oldUser.setEmail(newUser.getEmail());
                oldUser.setLogin(newUser.getLogin());
                oldUser.setName(newUser.getName());
                oldUser.setBirthday(newUser.getBirthday());

                log.info("Пользователь \"{}\" успешно изменен.", oldUser.getName());

                return oldUser;
            }
        }

        log.info("Пользователь не найден.");

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с id = " + newUser.getId() + " не найден.");
    }

    public Collection<User> getAllUsers() {
        log.info("Вывод всех пользователей.");

        return users.values();
    }

    private boolean isValidated(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()
                || !user.getEmail().contains("@")) {
            throw new ValidationException("Неверная почта!");
        }

        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }

        return true;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);

        log.debug("Максимальное используемое id: {}", currentMaxId);
        log.debug("Доступное максимальное id: {}", currentMaxId + 1);

        return ++currentMaxId;
    }
}
