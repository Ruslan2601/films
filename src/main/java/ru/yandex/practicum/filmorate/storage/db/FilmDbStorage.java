package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository("FilmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query(
                "SELECT film_id, name, description, release_date, duration, mpa_id FROM films", new FilmMapper());
        log.info("Отображаем список фильмов");
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update(
                "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        Film thisFilm = jdbcTemplate.queryForObject(
                "SELECT film_id, name, description, release_date, duration, mpa_id FROM films WHERE name=? "
                        + "AND description=? AND release_date=? AND duration=? AND mpa_id=?",
                new FilmMapper(), film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId());
        log.info("Добавлен фильм {}", thisFilm);
        return thisFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(
                "UPDATE films SET name=?, description=?, release_date=?, duration=?, mpa_id=? WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        Film thisFilm = getFilmById(film.getId());
        log.info("Обновлены данные фильма {}", thisFilm);
        return thisFilm;
    }

    @Override
    public Film getFilmById(int id) {
        try {
            Film thisFilm = jdbcTemplate.queryForObject(
                    "SELECT film_id, name, description, release_date, duration, mpa_id FROM films WHERE film_id=?",
                    new FilmMapper(), id);
            log.info("Фильм с id = '{}' найден", id);
            return thisFilm;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Фильма с таким id нет");
        }
    }

    @Override
    public Film putLike(int id, int userId) {
        jdbcTemplate.update(
                "INSERT INTO likes (film_id, user_id) VALUES (?, ?)",
                id, userId);
        Film film = getFilmById(id);
        log.info("Лайк добавлен фильму '{}'", film);
        return film;
    }

    @Override
    public Film deleteLike(int id, int userId) {
        jdbcTemplate.update(
                "DELETE from likes where film_id=? and user_id=?",
                id, userId);
        Film film = getFilmById(id);
        log.info("Лайк удален у фильма '{}'", film);
        return film;
    }

    @Override
    public List<Film> getPopularFilm(Integer count) {
        List<Integer> filmIds = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(
                "SELECT f.film_id FROM films as f " +
                        "left join likes as l on f.film_id = l.film_id group by f.film_id" +
                        " order by count(user_id) desc limit ?",
                count);
        while (userRows.next()) {
            Integer filmId = userRows.getInt("film_id");
            filmIds.add(filmId);
        }
        log.info("Отображен список самых популярных фильмов");
        return filmIds.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    @Override
    public void addGenres(int filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO films_genre (film_id, genre_id) VALUES (?, ?)", filmId, genre.getId());
            log.info("Добавлен жанр для фильма с id {}", filmId);
        }
    }

    @Override
    public void updateGenres(int filmId, Set<Genre> genres) {
        log.info("Обновлены жанры с id {} для фильма с id {}", filmId, genres);
        deleteGenres(filmId);
        addGenres(filmId, genres);
    }

    @Override
    public Set<Genre> getGenres(int filmId) {
            Set<Genre> genreList = new HashSet<>(jdbcTemplate.query(
                    "select f.genre_id, g.name from films_genre as f join genres g on g.genre_id = f.genre_id" +
                            " where f.film_id=? order by f.genre_id;",
                    new GenreMapper(), filmId));
            log.info("Отображены жанры фильма с id {}", filmId);
            return genreList;
    }

    @Override
    public void deleteGenres(int filmId) {
        jdbcTemplate.update(
                "DELETE from films_genre WHERE film_id=?",
                filmId);
        log.info("Удалены все жанры для фильма с id {}", filmId);
    }
}
