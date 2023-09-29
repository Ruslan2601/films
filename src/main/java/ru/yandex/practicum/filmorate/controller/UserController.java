package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    protected final Map<Integer, User> userMap = new HashMap<>();

    @GetMapping
    public List<User> getFilms() {
        return new ArrayList<>(userMap.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь");
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        userMap.put(user.getId(), user);
        log.info("Обновлены данные по пользователю");
        return user;
    }
}
