package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public List<Genre> getGenreList() {
        return genreStorage.getGenreList();
    }

    public Genre getGenreById(int id) {
        if (id < 0) {
            throw new ObjectNotFoundException("Id не может быть меньше 0");
        }
        return genreStorage.getGenreById(id);
    }
}
