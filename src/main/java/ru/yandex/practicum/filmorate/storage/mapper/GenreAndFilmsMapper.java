package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmsGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreAndFilmsMapper implements RowMapper<FilmsGenre> {
    @Override
    public FilmsGenre mapRow(ResultSet rs, int rowNum) throws SQLException {

        FilmsGenre genre = new FilmsGenre();
        genre.setGenreId(rs.getInt("genre_id"));
        genre.setFilmId(rs.getInt("film_id"));
        genre.setName(rs.getString("name"));
        return genre;
    }
}
