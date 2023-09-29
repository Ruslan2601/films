package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    protected final Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> getFilms() {
        return new ArrayList<>(userMap.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Обновлены данные по пользователю");
        } else {
            throw new ValidationException("Пользователя с такими id нет");
        }

        return user;
    }
}
