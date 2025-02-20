package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public UserDto updateUser(@RequestBody User newUser) {
        return userService.updateUser(newUser);
    }

    @DeleteMapping("/{userId}")
    public boolean deleteUser(@PathVariable Integer userId) {
        return userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public UserDto addFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable Integer userId, @PathVariable Integer friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<UserDto> getUserFriends(@PathVariable Integer userId) {
        return userService.getUserFriends(userId);
    }

    @GetMapping("/{user1Id}/friends/common/{user2Id}")
    public List<UserDto> getMutualFriends(@PathVariable Integer user1Id, @PathVariable Integer user2Id) {
        return userService.getMutualFriends(user1Id, user2Id);
    }
}
