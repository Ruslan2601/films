package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidation {
    public static void validation(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Ошибка в названии почты");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Ошибка в названии логина");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
            log.info("Поле для имени не было заполнены, был вставлен логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка в дате рождения");
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
