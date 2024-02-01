package ru.yandex.practicum.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserDto;
import ru.yandex.practicum.user.UserMapper;
import ru.yandex.practicum.user.UserRepository;
import ru.yandex.practicum.user.service.UserServiceAdmin;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceAdminImpl implements UserServiceAdmin {
    final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User userToSave = UserMapper.toModel(userDto);
        User userSaved = userRepository.save(userToSave);
        return UserMapper.toDto(userSaved);
    }

    @Override
    public UserDto getById(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllFiltered(Pageable pageable) {
        List<User> categoryList = userRepository.findAll(pageable).getContent();
        return UserMapper.listToDtoList(categoryList);
    }

    @Override
    public List<UserDto> getAllByIdInFiltered(List<Long> users, Pageable pageable) {
        List<User> categoryList = userRepository.findAllByIdIn(users, pageable);
        return UserMapper.listToDtoList(categoryList);
    }

    /*@Override
    public CategoryDto patch(CategoryDto categoryDto, long catId) {
        Category categoryToChange = userRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        CategoryMapper.updateByDto(categoryToChange, categoryDto);
        Category categorySaved = userRepository.save(categoryToChange);
        return CategoryMapper.toFullInfoDto(categorySaved);
    }*/

    @Override
    public void delete(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
