import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import ru.yandex.practicum.filmorate.SpringServer;
import ru.yandex.practicum.filmorate.dal.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ContextConfiguration(classes = {SpringServer.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class, MpaRowMapper.class})
public class SpringServerMpaDbStorageTests {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1);

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> assertThat(mpa)
                        .hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpas = mpaStorage.getAllMpa();

        assertThat(mpas)
                .extracting(Mpa::getName)
                .containsExactlyInAnyOrder("G", "PG", "PG-13", "R", "NC-17");
    }
}
