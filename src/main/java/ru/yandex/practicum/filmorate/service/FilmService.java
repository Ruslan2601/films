package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.FilmValidation;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;


    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        return setGenreAndMpa(films);
    }

    @Transactional
    public Film addFilm(@Valid Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        Film thisFilm = filmStorage.addFilm(film);
        filmStorage.addGenres(thisFilm.getId(), film.getGenres());
        thisFilm.setGenres(filmStorage.getGenres(thisFilm.getId()));
        thisFilm.setMpa(mpaStorage.getMpaById(thisFilm.getMpa().getId()));
        return thisFilm;
    }

    @Transactional
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

    @Transactional
    public Film putLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        return filmStorage.putLike(id, userId);
    }

    @Transactional
    public Film deleteLike(int id, int userId) {
        if (id < 0 || userId < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        return filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilm(Integer count) {
        List<Film> films = filmStorage.getPopularFilm(count);
        return setGenreAndMpa(films);
    }

    private List<Film> setGenreAndMpa(List<Film> films) {
        List<Mpa> mpaList = mpaStorage.getMpaList();
        List<FilmsGenre> genreList = genreStorage.getGenreListWithFilms();
        for (Film film : films) {
            Set<Genre> genres = new HashSet<>();
            genreList.stream().filter(x->x.getFilmId()==film.getId()).collect(Collectors.toList())
                    .forEach(x->genres.add(new Genre(x.getGenreId(), x.getName())));
            film.setGenres(genres);
            film.setMpa(mpaList.stream().filter(x -> x.getId() == film.getMpa().getId()).findFirst().get());
        }
        return films;
    }
}
