import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class UserServiceTest {
    private static UserService userService;
    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        userController = new UserController(userService);
    }
    
    @Test
    void shouldAddOneFriend() {
        User user = new User();
        user.setEmail("sdgfhjsdhf@mail.ru");
        user.setLogin("Kott");
        user.setName("Alex");
        user.setBirthday(LocalDate.of(2002, 5, 23));
        userController.addUser(user);

        User userFriend = new User();
        userFriend.setEmail("dsfsd@mail.ru");
        userFriend.setLogin("Dog");
        userFriend.setName("Oleg");
        userFriend.setBirthday(LocalDate.of(2001, 2, 11));
        userController.addUser(userFriend);

        userService.addFriend(user.getId(), userFriend.getId());
        
        assertEquals(1, userFriend.getFriends().size());
        assertEquals(1, user.getFriends().size());
    }

    @Test
    void getAllMutualFriends() {
        User user1 = new User();
        user1.setEmail("sdgfhjsdhf@mail.ru");
        user1.setLogin("Kott");
        user1.setName("Alex");
        user1.setBirthday(LocalDate.of(2002, 5, 23));
        userController.addUser(user1);

        User user2 = new User();
        user2.setEmail("dsfsd@mail.ru");
        user2.setLogin("Dog");
        user2.setName("Oleg");
        user2.setBirthday(LocalDate.of(2001, 2, 11));
        userController.addUser(user2);

        User userMutual1 = new User();
        userMutual1.setEmail("qweqwe@mail.ru");
        userMutual1.setLogin("Bird");
        userMutual1.setName("Alisa");
        userMutual1.setBirthday(LocalDate.of(2000, 1, 22));
        userController.addUser(userMutual1);

        User userMutual2 = new User();
        userMutual2.setEmail("vcbvb@mail.ru");
        userMutual2.setLogin("Bear");
        userMutual2.setName("Misha");
        userMutual2.setBirthday(LocalDate.of(2004, 5, 5));
        userController.addUser(userMutual2);

        userController.addFriend(user1.getId(), userMutual1.getId());
        userController.addFriend(user1.getId(), userMutual2.getId());

        userController.addFriend(user2.getId(), userMutual1.getId());
        userController.addFriend(user2.getId(), userMutual2.getId());

        System.out.println(userController.getMutualFriends(user1.getId(), user2.getId()));

        List<User> expectedFriends = List.of(userMutual1, userMutual2);
        List<User> actualFriends = new ArrayList<>(userController.getMutualFriends(user1.getId(), user2.getId()));

        assertEquals(expectedFriends, actualFriends);
    }
}
