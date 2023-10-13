package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.InternalServiceException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.FilmValidation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {


    protected final Map<Integer, Film> filmMap = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public Film addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        film.setId(++id);
        filmMap.put(film.getId(), film);
        log.info("Добавлен фильм");
        return film;
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        FilmValidation.validation(film, bindingResult);
        if (filmMap.containsKey(film.getId())) {
            filmMap.put(film.getId(), film);
            log.info("Обновлены данные по фильму");
        } else {
            throw new InternalServiceException("Фильма с такими id нет");
        }
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        if (filmMap.containsKey(id)) {
            log.info("Фильм с id = '{}' найден", id);
            return filmMap.get(id);
        } else {
            throw new ObjectNotFoundException("Фильма с таким id нет");
        }
    }
}
