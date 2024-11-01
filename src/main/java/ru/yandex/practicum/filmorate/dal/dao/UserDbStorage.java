package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> {
    private static final String FIND_ALL_USERS = "SELECT * FROM \"User\";";
    private static final String FIND_USER_BY_ID = "SELECT * FROM \"User\" WHERE id = ?;";
    private static final String INSERT_USER = "INSERT INTO \"User\" (EMAIL, LOGIN, NAME, BIRTHDAY) \n" +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE \"User\" SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?;";
    private static final String DELETE_USER = "DELETE FROM \"User\" WHERE id = ?;";
    private static final String DELETE_USER_FRIENDS = "DELETE FROM user_friends WHERE user_id = ?;";
    private static final String DELETE_USER_FILM_LIKES = "DELETE FROM film_likes WHERE user_id = ?;";
    private static final String ADD_FRIEND = "INSERT INTO user_friends VALUES (?, ?);";
    private static final String REMOVE_FRIEND = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?;";
    private static final String FIND_USER_FRIENDS = "SELECT * FROM \"User\" u\n" +
            "LEFT JOIN USER_FRIENDS uf ON u.ID = uf.FRIEND_ID\n" +
            "WHERE uf.USER_ID = ?;";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public Optional<User> getUserById(Integer userId) {
        return findOne(FIND_USER_BY_ID, userId);
    }

    public List<User> getAllUsers() {
        return findMany(FIND_ALL_USERS);
    }

    public User addUser(User user) {
        Integer id = insert(INSERT_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );

        user.setId(id);

        return user;
    }

    public User updateUser(User newUser) {
        if (findOne(FIND_USER_BY_ID, newUser.getId()).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }

        update(UPDATE_USER,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );

        return newUser;
    }

    public boolean deleteUser(Integer userId) {
        delete(DELETE_USER_FRIENDS, userId);

        delete(DELETE_USER_FILM_LIKES, userId);

        return delete(DELETE_USER, userId);
    }

    public Optional<User> addFriend(Integer userId, Integer friendId) {
        Optional<User> user = findOne(FIND_USER_BY_ID, userId);
        Optional<User> friend = findOne(FIND_USER_BY_ID, friendId);

        if (user.isPresent() && friend.isPresent()) {
            insert(ADD_FRIEND,
                    userId,
                    friendId
            );

            return user;
        }

        throw new NotFoundException("Пользователь не найден");
    }

    public Optional<User> removeFriend(Integer userId, Integer friendId) {
        Optional<User> user = findOne(FIND_USER_BY_ID, userId);
        Optional<User> friend = findOne(FIND_USER_BY_ID, friendId);

        if (user.isPresent() && friend.isPresent()) {
            delete(REMOVE_FRIEND,
                    userId,
                    friendId
            );

            return user;
        }

        throw new NotFoundException("Пользователь не найден");
    }

    public List<User> getUserFriends(Integer userId) {
        if (findOne(FIND_USER_BY_ID, userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден.");
        }

        return findMany(FIND_USER_FRIENDS, userId);
    }
}
