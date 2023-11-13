package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(int id);

    Film putLike(int id, int userId);

    Film deleteLike(int id, int userId);

    List<Film> getPopularFilm(Integer count);

    void addGenres(int filmId, Set<Genre> genres);

    void updateGenres(int filmId, Set<Genre> genres);

    Set<Genre> getGenres(int filmId);

    void deleteGenres(int filmId);
}
