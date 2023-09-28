package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidation {
    public static void validation(Film film) {
        if (film.getName().isEmpty() || film.getName() == null) {
            log.error("Ошибка в названии фильма(пустое значение)");
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getName().length()>200) {
            log.error("Ошибка в названии фильма(слишком длинное название)");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1985, 12, 28))) {
            log.error("Фильм вышел раньше установленной даты");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration()<=0) {
            log.error("Ошибка в продолжительности фильма");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
