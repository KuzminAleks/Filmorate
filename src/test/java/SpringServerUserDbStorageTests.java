import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.SpringServer;
import ru.yandex.practicum.filmorate.dal.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {SpringServer.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class SpringServerUserDbStorageTests {
    private final UserDbStorage userStorage;

    @Test
    public void testGetUserById() {
        Optional<User> userOptional = userStorage.getUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user)
                        .hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = userStorage.getAllUsers();

        assertThat(users)
                .hasSize(4)
                .extracting(User::getLogin)
                .containsExactlyInAnyOrder("User1", "User2", "User3", "User4");
    }

    @Test
    public void testAddUser() {
        User user = new User();

        user.setEmail("test@mail.ru");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User user1 = userStorage.addUser(user);

        assertThat(user1)
                .hasFieldOrPropertyWithValue("email", "test@mail.ru");
    }

    @Test
    public void testUpdateUser() {
        User user = new User();

        user.setEmail("test@mail.ru");
        user.setLogin("testLogin");
        user.setName("testName");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userStorage.addUser(user);

        User userUpdated = new User();

        userUpdated.setId(user.getId());
        userUpdated.setEmail("newEmail@mail.ru");
        userUpdated.setLogin("newTestLogin");
        userUpdated.setName("newTestName");
        userUpdated.setBirthday(LocalDate.of(2010, 2, 2));

        userStorage.updateUser(userUpdated);

        assertThat(userUpdated)
                .hasFieldOrPropertyWithValue("email", "newEmail@mail.ru")
                .hasFieldOrPropertyWithValue("login", "newTestLogin")
                .hasFieldOrPropertyWithValue("name", "newTestName")
                .hasFieldOrPropertyWithValue("birthday", LocalDate.of(2010, 2, 2));
    }

    @Test
    public void testDeleteUser() {
        User user = new User();

        user.setEmail("test@mail.ru");
        user.setLogin("testDeleteUser");
        user.setName("testName");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        userStorage.addUser(user);

        List<User> users = userStorage.getAllUsers();

        assertThat(users)
                .extracting(User::getLogin)
                .contains("testDeleteUser");

        userStorage.deleteUser(user.getId());

        users = userStorage.getAllUsers();

        assertThat(users)
                .extracting(User::getLogin)
                .doesNotContain("testDeleteUser");
    }

    @Test
    public void testAddFriend() {
        User user1 = new User();

        user1.setEmail("test@mail.ru");
        user1.setLogin("testUser1");
        user1.setName("testName1");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        User user2 = new User();

        user2.setEmail("test@mail.ru");
        user2.setLogin("testUser2");
        user2.setName("testName2");
        user2.setBirthday(LocalDate.of(2008, 3, 23));

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        userStorage.addFriend(user1.getId(), user2.getId());

        List<User> friends = userStorage.getUserFriends(user1.getId());

        assertThat(friends)
                .contains(user2);
    }

    @Test
    public void testRemoveFriend() {
        User user1 = new User();

        user1.setEmail("test@mail.ru");
        user1.setLogin("testUser1");
        user1.setName("testName1");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        User user2 = new User();

        user2.setEmail("test@mail.ru");
        user2.setLogin("testUser2");
        user2.setName("testName2");
        user2.setBirthday(LocalDate.of(2008, 3, 23));

        userStorage.addUser(user1);
        userStorage.addUser(user2);

        userStorage.addFriend(user1.getId(), user2.getId());

        List<User> friends = userStorage.getUserFriends(user1.getId());

        assertThat(friends)
                .contains(user2);

        userStorage.removeFriend(user1.getId(), user2.getId());

        friends = userStorage.getUserFriends(user1.getId());

        assertThat(friends)
                .doesNotContain(user2);
    }
}