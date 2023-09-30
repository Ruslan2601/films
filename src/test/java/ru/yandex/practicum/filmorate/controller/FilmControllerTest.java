package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class FilmControllerTest {
    @Mock
    private BindingResult bindingResult;

    private final FilmController filmController = new FilmController();

    private final Film film = new Film(1, "Matrix", "dex",
            LocalDate.of(2011, 10, 22), 107);
    private final Film film2 = new Film(2, "Matrix 2", "des",
            LocalDate.of(2015, 10, 22), 127);

    @Test
    public void getFilms_Size() {
        assertEquals(0, filmController.getFilms().size(), "Список не пуст");
        filmController.filmMap.put(film.getId(), film);
        assertEquals(1, filmController.getFilms().size(), "Неверный размер списка");
    }

    @Test
    public void addFilm_Equals() {
        filmController.addFilm(film, bindingResult);
        assertEquals(film, filmController.filmMap.get(film.getId()), "Объекты неравны");
    }

    @Test
    public void updateFilm_Equals() {
        filmController.addFilm(film, bindingResult);
        film.setName("Max");
        filmController.updateFilm(film, bindingResult);
        assertEquals(film, filmController.filmMap.get(film.getId()), "Объекты неравны");
    }

    @Test
    public void addFilm_WrongDate() {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        assertThrows(ValidationException.class,
                () -> filmController.addFilm(film, bindingResult), "Запрос прошел без ошибки");
    }

    @Test
    public void addFilm_EmptyFieldName() {
        film.setName("");
        assertThrows(ValidationException.class,
                () -> filmController.addFilm(film, bindingResult), "Запрос прошел без ошибки");
    }

    @Test
    public void addFilm_ToMuchDescription() {
        film.setDescription("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        assertThrows(ValidationException.class,
                () -> filmController.addFilm(film, bindingResult), "Запрос прошел без ошибки");
    }

    @Test
    public void addFilm_NegativeDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class,
                () -> filmController.addFilm(film, bindingResult), "Запрос прошел без ошибки");
    }

    @Test
    public void addFilm_Empty() {
        assertThrows(ValidationException.class,
                () -> filmController.addFilm(new Film(), bindingResult), "Запрос прошел без ошибки");
    }
}