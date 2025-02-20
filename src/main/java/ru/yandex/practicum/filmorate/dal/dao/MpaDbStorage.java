package ru.yandex.practicum.filmorate.dal.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseDbStorage<Mpa> {
    private static final String FIND_ALL_MPA = "SELECT * FROM mpa;";
    private static final String FIND_MPA_BY_ID = "SELECT * FROM mpa WHERE id = ?;";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_MPA);
    }

    public Optional<Mpa> getMpaById(Integer mpaId) {
        return findOne(FIND_MPA_BY_ID, mpaId);
    }
}
