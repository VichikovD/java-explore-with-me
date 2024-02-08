package ru.yandex.practicum.user.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto getById(long userId);

    List<UserDto> getAllFiltered(Pageable pageable);

    List<UserDto> getAllByIdInFiltered(List<Long> users, Pageable pageable);

    void delete(long userId);
}
