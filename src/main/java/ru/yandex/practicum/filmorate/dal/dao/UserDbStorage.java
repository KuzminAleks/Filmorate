package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseDbStorage<User> {
    private static final String FIND_ALL_USERS = "SELECT * FROM \"User\"";
    private static final String FIND_USER_BY_ID = "SELECT * FROM \"User\" WHERE id = ?;";
    private static final String INSERT_USER = "INSERT INTO \"User\" (EMAIL, LOGIN, NAME, BIRTHDAY) \n" +
            "VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER = "UPDATE \"User\" SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM \"User\" WHERE id = ?";

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
        return delete(DELETE_USER, userId);
    }
}
