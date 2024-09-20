package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserController userStorage = new UserController();

    public User addFriend(User user, User userFriend) {
        if (user.getFriends().contains(userFriend.getId())) {
            return null;
        } else {
            user.getFriends().add(userFriend.getId());
            userFriend.getFriends().add(user.getId());
        }

        return user;
    }

    public User deleteFriend(User user, User userFriend) {
        if (user.getFriends().remove(userFriend.getId())) {
            userFriend.getFriends().remove(user.getId());

            return user;
        }

        return null;
    }

    public Collection<User> getMutualFriends(User user, User userFriend) {
        Set<Integer> mutualFriends = user.getFriends().stream()
                .filter(friend -> userFriend.getFriends().contains(friend))
                .collect(Collectors.toSet());

        Collection<User> userArrayList = userStorage.getAllUsers();

        for (User users : userArrayList) {
            System.out.println(users);

            if (mutualFriends.remove(users.getId())) {
                System.out.println(users + " - общий.");
            }
        }
        return null;
//        return userStorage.getAllUsers().stream()
//                .filter(users -> mutualFriends.remove(users.getId()))
//                .collect(Collectors.toList());
    }
}
