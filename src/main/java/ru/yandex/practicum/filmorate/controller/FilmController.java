package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        return filmService.addFilm(film, bindingResult);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        return filmService.updateFilm(film, bindingResult);
    }

    @GetMapping("/{id}")
    public Film getFilmByID(@PathVariable("id") Integer id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film putLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilm(count);
    }
}