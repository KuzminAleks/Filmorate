import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.dao.FilmDbStorage;

public class FilmDbStorageTest {
    @Autowired
    private static FilmDbStorage filmDbStorage;

    @BeforeEach
    void beforeEach() {
    }

    @Test
    void test() {
        System.out.println(filmDbStorage.getCount());
    }

}
