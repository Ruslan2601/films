package ru.yandex.practicum.filmorate.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;


public class FilmValidation {
    public static void validation(Film film, BindingResult bindingResult) {
        if (film.getReleaseDate() == null ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getName().isEmpty() || film.getName() == null) {
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length()>200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getDuration()<=0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField()).append(" - ")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new ValidationException(errorMsg.toString());
        }
    }
}
