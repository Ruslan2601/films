package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    Film addFilm(Film film, BindingResult bindingResult);

    Film updateFilm(Film film, BindingResult bindingResult);

    Film getFilmById(int id);
}
