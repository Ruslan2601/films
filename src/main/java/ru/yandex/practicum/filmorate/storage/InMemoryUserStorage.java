package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    protected final Map<Integer, User> userMap = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User addUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        user.setId(++id);
        userMap.put(user.getId(), user);
        log.info("Добавлен пользователь");
        return user;
    }

    @Override
    public User updateUser(User user, BindingResult bindingResult) {
        UserValidation.validation(user, bindingResult);
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            log.info("Обновлены данные по пользователю");
        } else {
            throw new ObjectNotFoundException("Пользователя с такими id нет");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (userMap.containsKey(id)) {
            log.info("Пользователь с id = '{}' найден", id);
            return userMap.get(id);
        } else {
            throw new ObjectNotFoundException("Пользователя с такими id нет");
        }
    }

}
