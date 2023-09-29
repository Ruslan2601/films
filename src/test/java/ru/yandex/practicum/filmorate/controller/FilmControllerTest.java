package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(0, filmController.getUsers().size(), "Список не пуст");
        filmController.filmMap.put(film.getId(), film);
        assertEquals(1, filmController.getUsers().size(), "Неверный размер списка");
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
}