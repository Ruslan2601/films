package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidation;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    protected final Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        film.setId(++id);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Обновлены данные по фильму");
        } else {
            throw new NotFoundException("Фильма с такими id нет");
        }
        return film;
    }
}