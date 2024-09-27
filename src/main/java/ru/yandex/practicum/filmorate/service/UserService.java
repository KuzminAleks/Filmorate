package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addFriend(Integer userId, Integer userFriendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(userFriendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }

        if (userFriend == null) {
            throw new NotFoundException("Пользователь с id: " + userFriendId + " не найден.");
        }

        log.info(user.toString());
        log.info(userFriend.toString());

        user.getFriends().add(userFriendId);
        userFriend.getFriends().add(userId);

        log.info(user.toString());
        log.info(userFriend.toString());

        return user;
    }

    public Collection<User> getUserFriends(Integer userId) {
        log.trace("Пользователь: " + userStorage.getUserById(userId).toString());
        log.trace("Друзья пользователя: " + userStorage.getUserById(userId).getFriends().toString());

        return userStorage.getAllUsers().stream()
                .filter(user -> userStorage.getUserById(userId).getFriends().contains(user.getId()))
                .collect(Collectors.toList());
    }

    public User deleteFriend(Integer userId, Integer userFriendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(userFriendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден.");
        }

        if (userFriend == null) {
            throw new NotFoundException("Пользователь с id: " + userFriendId + " не найден.");
        }

        user.getFriends().remove(userFriendId);
        userFriend.getFriends().remove(userId);

        return user;
    }

    public Collection<User> getMutualFriends(Integer user1Id, Integer user2Id) {
        User user1 = userStorage.getUserById(user1Id);
        User user2 = userStorage.getUserById(user2Id);

        Set<Integer> mutualFriends = user1.getFriends().stream()
                .filter(friend -> user2.getFriends().contains(friend))
                .collect(Collectors.toSet());

        return userStorage.getAllUsers().stream()
                .filter(users -> mutualFriends.remove(users.getId()))
                .collect(Collectors.toList());
    }
}
