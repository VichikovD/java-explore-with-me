package ru.yandex.practicum.user;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User toModel(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static List<UserDto> listToDtoList(List<User> userList) {
        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : userList) {
            userDtoList.add(toDto(user));
        }
        return userDtoList;
    }

    public static void updateByDto(User user, UserDto userDto) {
        user.setName(userDto.getName());
        user.setEmail(user.getEmail());
    }
}
