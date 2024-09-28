import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void shouldNotPassValidationByEmail() {
        User someUser = new User();
        someUser.setEmail("sdgfhjsdhfmail.ru");
        someUser.setLogin("Kott");
        someUser.setName("Alex");
        someUser.setBirthday(LocalDate.of(2002, 5, 23));

        assertThrows(ValidationException.class, () -> userController.addUser(someUser));

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void shouldNotPassValidationByLogin() {
        User someUser = new User();
        someUser.setEmail("sdgfhjsdhf@mail.ru");
        someUser.setLogin("Kott Pushok");
        someUser.setName("Alex");
        someUser.setBirthday(LocalDate.of(2002, 5, 23));

        assertThrows(ValidationException.class, () -> userController.addUser(someUser));

        assertTrue(userController.getAllUsers().isEmpty());
    }

    @Test
    void shouldNotPassValidationByBirthday() {
        User someUser = new User();
        someUser.setEmail("sdgfhjsdhf@mail.ru");
        someUser.setLogin("Kott_Pushok");
        someUser.setName("Alex");
        someUser.setBirthday(LocalDate.now().plusMonths(2));

        assertThrows(ValidationException.class, () -> userController.addUser(someUser));

        assertTrue(userController.getAllUsers().isEmpty());
    }
}
