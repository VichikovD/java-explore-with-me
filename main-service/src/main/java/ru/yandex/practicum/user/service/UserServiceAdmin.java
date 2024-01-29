package ru.yandex.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.user.UserDto;

import java.util.List;

public interface UserServiceAdmin {
    UserDto create(UserDto userDto);

    public UserDto getById(long userId);

    public List<UserDto> getFiltered(Pageable pageable);

    void delete(long userId);
}
