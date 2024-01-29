package ru.yandex.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.OffsetPageable;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.service.UserServiceAdmin;

import java.util.List;

@Slf4j
@RequestMapping("/admin/users")
@RestController
@RequiredArgsConstructor
@Validated
public class UserControllerAdmin {
    final UserServiceAdmin userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Validated UserDto userDto) {
        log.info("POST \"/admin/users\" Body={}", userDto);
        UserDto userToReturn = userService.create(userDto);
        log.debug("Created user=" + userToReturn);
        return userToReturn;
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable(name = "userId") long userId) {
        log.info("GET \"/admin/users/{}", userId);
        UserDto user = userService.getById(userId);
        log.debug("user = " + user);
        return user;
    }

    @GetMapping
    public List<UserDto> getFiltered(@RequestParam(name = "from", defaultValue = "0") int offset,
                                     @RequestParam(name = "size", defaultValue = "10") int limit) {
        log.info("GET \"/admin/users?from={}&size={}\"", offset, limit);
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = new OffsetPageable(offset, limit, sort);
        List<UserDto> userList = userService.getFiltered(pageable);
        log.debug("UserList = " + userList);
        return userList;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "userId") long userId) {
        log.info("DELETE \"/admin/users/{}\"", userId);
        userService.delete(userId);
        log.debug("Deleted user with id=" + userId);
    }
}
