package ru.yandex.practicum.user.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void create() {
        UserDto userDtoReceived = getUserDtoNullId();
        User userReturnedFromDB = getUser();
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(userReturnedFromDB);

        UserDto actualUserDto = userService.create(userDtoReceived);

        assertThat(actualUserDto.getId(), is(1L));
        assertThat(actualUserDto.getEmail(), is("user@email.ru"));
        assertThat(actualUserDto.getName(), is("name"));
        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getById_whenRepositoryReturnOptionalEmpty_thenThrowsException() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.getById(1L));

        assertThat(exception.getMessage(), is("User with id=1 was not found"));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void getById() {
        User userReturnedFromDB = getUser();
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(userReturnedFromDB));

        UserDto actualUserDto = userService.getById(1L);

        assertThat(actualUserDto.getId(), is(1L));
        assertThat(actualUserDto.getName(), is("name"));
        assertThat(actualUserDto.getEmail(), is("user@email.ru"));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(1L);
    }

    @Test
    void getAllFiltered() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new OffsetPageable(1, 1, sort);
        User user = new User(2L, "name", "email@user.com2");
        List<User> users = List.of(user);
        Page pageFromDB = new PageImpl(users, pageable, 1L);
        Mockito.when(userRepository.findAll(pageable))
                .thenReturn(pageFromDB);

        List<UserDto> actualUserDtoList = userService.getAllFiltered(pageable);
        UserDto actualUserDto = actualUserDtoList.get(0);

        assertThat(actualUserDtoList.size(), is(1));
        assertThat(actualUserDto.getId(), is(2L));
        assertThat(actualUserDto.getEmail(), is("email@user.com2"));
        assertThat(actualUserDto.getName(), is("name"));
    }

    @Test
    void getAllByIdInFiltered() {
    }

    @Test
    void delete() {
    }

    private UserDto getUserDtoNullId() {
        return UserDto.builder()
                .id(null)
                .email("user@email.ru")
                .name("name")
                .build();
    }

    private User getUserNullId() {
        return User.builder()
                .id(null)
                .email("user@email.ru")
                .name("name")
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("user@email.ru")
                .name("name")
                .build();
    }
}