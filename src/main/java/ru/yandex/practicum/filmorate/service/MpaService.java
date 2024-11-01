package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.dao.MpaDbStorage;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.dal.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MpaService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public List<MpaDto> getAllMpa() {
        return mpaDbStorage.getAllMpa().stream()
                .map(MpaMapper::mapToMpaDto)
                .collect(Collectors.toList());
    }

    public MpaDto getMpaById(Integer mpaId) {
        Optional<Mpa> mpa = mpaDbStorage.getMpaById(mpaId);

        log.debug(mpa.toString());

        return mpa.map(MpaMapper::mapToMpaDto).orElseThrow(() -> new NotFoundException("Такого MPA не существует."));
    }
}
