package ru.yandex.practicum.filmorate.dal.mapper;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.dal.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public final class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setLogin(user.getLogin());
        userDto.setName(user.getName());
        userDto.setBirthday(user.getBirthday());

        return userDto;
    }
}
