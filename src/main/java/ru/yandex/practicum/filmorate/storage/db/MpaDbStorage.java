package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;

@Slf4j
@Repository("MpaDbStorage")
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getMpaList() {
        List<Mpa> mpaList = jdbcTemplate.query(
                "SELECT mpa_id, rating FROM mpa", new MpaMapper());
        log.info("Отображаем список рейтингов");
        return mpaList;
    }

    @Override
    public Mpa getMpaById(int id) {
        try {
            Mpa mpa = jdbcTemplate.queryForObject(
                    "SELECT mpa_id, rating FROM mpa WHERE mpa_id=?",
                    new MpaMapper(), id);
            log.info("Рейтинг с id = '{}' найден", id);
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Рейтинга с таким id нет");
        }
    }
}
