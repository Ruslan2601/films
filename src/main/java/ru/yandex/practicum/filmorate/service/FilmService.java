package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.FilmValidation;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    MpaStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        for (Film film: films) {
            film.setGenres(filmStorage.getGenres(film.getId()));
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        }
        return films;
    }

    public Film addFilm(@Valid Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        Film thisFilm = filmStorage.addFilm(film);
        filmStorage.addGenres(thisFilm.getId(), film.getGenres());
        thisFilm.setGenres(filmStorage.getGenres(thisFilm.getId()));
        thisFilm.setMpa(mpaStorage.getMpaById(thisFilm.getMpa().getId()));
        return thisFilm;
    }

    public Film updateFilm(@Valid Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        Film thisFilm = filmStorage.updateFilm(film);
        filmStorage.updateGenres(thisFilm.getId(), film.getGenres());
        thisFilm.setGenres(filmStorage.getGenres(thisFilm.getId()));
        thisFilm.setMpa(mpaStorage.getMpaById(thisFilm.getMpa().getId()));
        return thisFilm;
    }

    public Film getFilmById(int id) {
        Film thisFilm = filmStorage.getFilmById(id);
        thisFilm.setGenres(filmStorage.getGenres(thisFilm.getId()));
        thisFilm.setMpa(mpaStorage.getMpaById(thisFilm.getMpa().getId()));
        return thisFilm;
    }

    public Film putLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        return filmStorage.putLike(id, userId);
    }

    public Film deleteLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        return filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilm(Integer count) {
        List<Film> films = filmStorage.getPopularFilm(count);
        for (Film film: films) {
            film.setGenres(filmStorage.getGenres(film.getId()));
            film.setMpa(mpaStorage.getMpaById(film.getMpa().getId()));
        }
        return films;
    }
}
