package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dal.dto.MpaDto;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
public final class MpaMapper {
    public static MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto mpaDto = new MpaDto();

        mpaDto.setId(mpa.getId());
        mpaDto.setName(mpa.getName());

        return mpaDto;
    }
}
