package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidation;

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
    public Film addFilm(@RequestBody Film film) {
        FilmValidation.validation(film);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        FilmValidation.validation(film);
        filmMap.put(film.getId(), film);
        log.info("Обновлены данные по фильму");
        return film;
    }
}