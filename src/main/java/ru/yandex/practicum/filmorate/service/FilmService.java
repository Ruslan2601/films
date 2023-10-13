package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film putLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        Film film = filmStorage.getFilmById(id);
        film.addLike(userId);
        log.info("Лайк добавлен фильму '{}'", film.getName());
        return film;
    }

    public Film deleteLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        Film film = filmStorage.getFilmById(id);
        film.removeLike(userId);
        log.info("Лайк удален у фильма '{}'", film.getName());
        return film;
    }

    public List<Film> getPopularFilm(Integer count) {
        log.info("Отображен список самых популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }
}
