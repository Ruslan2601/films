package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.List;

@Slf4j
@Repository("GenreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenreList() {
        List<Genre> genres = jdbcTemplate.query(
                "SELECT genre_id, name FROM genres", new GenreMapper());
        log.info("Отображаем список жанров");
        return genres;
    }

    @Override
    public Genre getGenreById(int id) {
        try {
            Genre thisGenre = jdbcTemplate.queryForObject(
                    "SELECT genre_id, name FROM genres WHERE genre_id=?",
                    new GenreMapper(), id);
            log.info("Фильм с id = '{}' найден", id);
            return thisGenre;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Жанра с таким id нет");
        }
    }
}
