package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmsGenre;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> getGenreList();

    Genre getGenreById(int id);

    List<FilmsGenre> getGenreListWithFilms();
}
