package ru.yandex.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.service.UserService;
import ru.yandex.practicum.util.OffsetPageable;

import java.util.List;

@Slf4j
@RequestMapping("/admin/users")
@RestController
@RequiredArgsConstructor
@Validated
public class UserAdminController {
    final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Validated UserDto userDto) {
        log.info("POST \"/admin/users\" Body={}", userDto);
        UserDto userToReturn = userService.create(userDto);
        log.debug("User created=" + userToReturn);
        return userToReturn;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") long userId) {
        log.info("GET \"/admin/users/{}", userId);
        UserDto user = userService.getById(userId);
        log.debug("User found= " + user);
        return user;
    }

    @GetMapping
    public List<UserDto> getFiltered(@RequestParam(name = "ids", required = false) List<Long> users,
                                     @RequestParam(name = "from", defaultValue = "0") int offset,
                                     @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/admin/users?users={}&from={}&size={}\"", users, offset, limit);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new OffsetPageable(offset, limit, sort);
        List<UserDto> userList;
        if (users == null) {
            userList = userService.getAllFiltered(pageable);
        } else {
            userList = userService.getAllByIdInFiltered(users, pageable);
        }
        log.debug("UserList found= " + userList);
        return userList;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "userId") long userId) {
        log.info("DELETE \"/admin/users/{}\"", userId);
        userService.delete(userId);
        log.debug("User deleted with id=" + userId);
    }
}
