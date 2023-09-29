package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ErrorResponse;
import ru.yandex.practicum.filmorate.util.FilmValidation;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> filmMap = new HashMap<>();

    @GetMapping
    public List<Film> getUsers() {
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        filmMap.put(film.getId(), film);
        log.info("Обновлены данные по фильму");
        return film;
    }
}