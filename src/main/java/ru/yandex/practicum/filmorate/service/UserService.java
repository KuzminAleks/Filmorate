package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.dal.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.dal.mapper.UserMapper.mapToUserDto;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserDbStorage userDbStorage;

    public UserService (UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public List<UserDto> getAllUsers() {
        return userDbStorage.getAllUsers().stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    public UserDto addUser(User user) {
        if (isValidated(user)) {
            log.debug(user.toString());

            return mapToUserDto(userDbStorage.addUser(user));
        }

        throw new InternalServerException("Пользователь не добавлен, что-то пошло не так.");
    }

    public UserDto updateUser(User user) {
        if (isValidated(user)) {
            log.debug(user.toString());

            return mapToUserDto(userDbStorage.updateUser(user));
        }

        throw new InternalServerException("Пользователь не обновлен, что-то пошло не так.");
    }

    public boolean deleteUser(Integer userId) {
        return userDbStorage.deleteUser(userId);
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










//    private final UserStorage userStorage;
//
//    private final Logger log = LoggerFactory.getLogger(UserService.class);
//
//    @Autowired
//    public UserService(InMemoryUserStorage userStorage) {
//        this.userStorage = userStorage;
//    }

//    public User addUser(User user) {
//        return userStorage.addUser(user);
//    }
//
//    public User updateUser(User user) {
//        return userStorage.updateUser(user);
//    }
//
//    public Collection<User> getAllUsers() {
//        return userStorage.getAllUsers();
//    }
//
//    public User addFriend(Integer userId, Integer userFriendId) {
//        User user = userStorage.getUserById(userId);
//        User userFriend = userStorage.getUserById(userFriendId);
//
//        log.debug("До добавления в друзья: {}", user);
//        log.debug("До добавления в друзья: {}", userFriend);
//
//        user.getFriends().add(userFriendId);
//        userFriend.getFriends().add(userId);
//
//        log.debug("После добавления в друзья: {}", user);
//        log.debug("После добавления в друзья: {}", userFriend);
//
//        return user;
//    }
//
//    public Collection<User> getUserFriends(Integer userId) {
//        log.trace("Пользователь: {}", userStorage.getUserById(userId));
//        log.trace("Друзья пользователя: {}", userStorage.getUserById(userId).getFriends());
//
//        return userStorage.getAllUsers().stream()
//                .filter(user -> userStorage.getUserById(userId).getFriends().contains(user.getId()))
//                .collect(Collectors.toList());
//    }
//
//    public User deleteFriend(Integer userId, Integer userFriendId) {
//        User user = userStorage.getUserById(userId);
//        User userFriend = userStorage.getUserById(userFriendId);
//
//        log.debug("Пользователь 1 до удаления из друзей: {}", user);
//        log.debug("Пользователь 2 до удаления из друзей: {}", userFriend);
//
//        user.getFriends().remove(userFriendId);
//        userFriend.getFriends().remove(userId);
//
//        log.debug("Пользователь 1 после удаления из друзей: {}", user);
//        log.debug("Пользователь 2 после удаления из друзей: {}", userFriend);
//
//        return user;
//    }
//
//    public Collection<User> getMutualFriends(Integer user1Id, Integer user2Id) {
//        User user1 = userStorage.getUserById(user1Id);
//        User user2 = userStorage.getUserById(user2Id);
//
//        log.debug("Пользователь 1: {}", user1);
//        log.debug("Пользователь 2: {}", user2);
//
//        Set<Integer> mutualFriends = user1.getFriends().stream()
//                .filter(friend -> user2.getFriends().contains(friend))
//                .collect(Collectors.toSet());
//
//        log.debug("Id общих друзей пользователей 1 и 2: {}", mutualFriends);
//
//        return userStorage.getAllUsers().stream()
//                .filter(users -> mutualFriends.remove(users.getId()))
//                .collect(Collectors.toList());
//    }
//
//    public boolean isFriends(Integer userId, Integer userFriendId) {
//        return userStorage.getUserById(userId).getFriends().contains(userFriendId);
//    }
}
