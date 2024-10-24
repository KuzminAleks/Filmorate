package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userId}/friends/{userFriendId}")
    public User addFriend(@PathVariable Integer userId, @PathVariable Integer userFriendId) {
        return userService.addFriend(userId, userFriendId);
    }

    @DeleteMapping("/{userId}/friends/{userFriendId}")
    public User deleteFriend(@PathVariable Integer userId, @PathVariable Integer userFriendId) {
        return userService.deleteFriend(userId, userFriendId);
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable Integer userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{user1Id}/friends/common/{user2Id}")
    public Collection<User> getMutualFriends(@PathVariable Integer user1Id, @PathVariable Integer user2Id) {
        return userService.getMutualFriends(user1Id, user2Id);
    }
}
