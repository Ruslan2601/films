package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalServiceException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {


    protected final Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Обновлены данные по фильму");
        } else {
            throw new InternalServiceException("Фильма с такими id нет");
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (filmMap.containsKey(id)) {
            log.info("Фильм с id = '{}' найден", id);
            return filmMap.get(id);
        } else {
            throw new ObjectNotFoundException("Фильма с таким id нет");
        }
    }

    @Override
    public Film putLike(int id, int userId) {
        Film film = getFilmById(id);
        film.addLike(userId);
        log.info("Лайк добавлен фильму '{}'", film.getName());
        return film;
    }

    @Override
    public Film deleteLike(int id, int userId) {
        Film film = getFilmById(id);
        film.removeLike(userId);
        log.info("Лайк удален у фильма '{}'", film.getName());
        return film;
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        log.info("Отображен список самых популярных фильмов");
        return getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public void addGenres(int filmId, Set<Genre> genres) {

    }

    @Override
    public void updateGenres(int filmId, Set<Genre> genres) {

    }

    @Override
    public Set<Genre> getGenres(int filmId) {
        return null;
    }

    @Override
    public void deleteGenres(int filmId) {

    }
}
