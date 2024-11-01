package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dal.mapper.UserMapper.mapToUserDto;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDbStorage userDbStorage;

    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public List<UserDto> getAllUsers() {
        return userDbStorage.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Integer userId) {
        Optional<User> user = userDbStorage.getUserById(userId);

        return user.map(UserMapper::mapToUserDto).orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }

    public UserDto addUser(User user) {
        log.debug(user.toString());

        if (isValidated(user)) {
            log.debug(user.toString());

            return mapToUserDto(userDbStorage.addUser(user));
        }

        throw new InternalServerException("Пользователь не добавлен, что-то пошло не так.");
    }

    public UserDto updateUser(User user) {
        log.debug(user.toString());

        if (isValidated(user)) {
            log.debug(user.toString());

            return mapToUserDto(userDbStorage.updateUser(user));
        }

        throw new InternalServerException("Пользователь не обновлен, что-то пошло не так.");
    }

    public boolean deleteUser(Integer userId) {
        return userDbStorage.deleteUser(userId);
    }

    public UserDto addFriend(Integer userId, Integer friendId) {
        Optional<User> user = userDbStorage.addFriend(userId, friendId);

        log.info(user.toString());

        return user.map(UserMapper::mapToUserDto).orElse(null);
    }

    public UserDto removeFriend(Integer userId, Integer friendId) {
        Optional<User> user = userDbStorage.removeFriend(userId, friendId);

        return user.map(UserMapper::mapToUserDto).orElse(null);
    }

    public List<UserDto> getUserFriends(Integer userId) {
        return userDbStorage.getUserFriends(userId).stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public List<UserDto> getMutualFriends(Integer user1Id, Integer user2Id) {
        List<User> user1Friends = userDbStorage.getUserFriends(user1Id);
        List<User> user2Friends = userDbStorage.getUserFriends(user2Id);

        return user2Friends.stream()
                .filter(user1Friends::contains)
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
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
}
