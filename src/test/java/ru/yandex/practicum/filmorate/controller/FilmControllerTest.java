package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerTest {

    private final Film film = new Film(1, "Matrix", "dex",
            LocalDate.of(2011, 10, 22), 107);

    private FilmController filmController = new FilmController();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddFilm_WrongDate() throws Exception {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("releaseDate - дата релиза — не раньше 1895-12-28;"));
    }

    @Test
    public void testAddFilm_ToMuchDescription() throws Exception {
        film.setDescription("d".repeat(201));
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("description - максимальная длина описания — 200 символов;"));
    }

    @Test
    public void testAddFilm_EmptyException() throws Exception {
        film.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("name - название не может быть пустым;"));
    }

    @Test
    public void testAddFilm_NegativeDuration() throws Exception {
        film.setDuration(-1);
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("duration - продолжительность фильма должна быть положительной;"));
    }

    @Test
    public void getFilms_Size() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(0));

        addFilm();

        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void getFilms_Equals() throws Exception {
        addFilm();
        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(List.of(film))));
    }

    @Test
    public void testAddFilm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Matrix"))
                .andExpect(jsonPath("$.description").value("dex"))
                .andExpect(jsonPath("$.duration").value(107))
                .andExpect(jsonPath("$.releaseDate").value("2011-10-22"));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        addFilm();
        film.setName("Matrix 2");
        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Matrix 2"))
                .andExpect(jsonPath("$.description").value("dex"))
                .andExpect(jsonPath("$.duration").value(107))
                .andExpect(jsonPath("$.releaseDate").value("2011-10-22"));
    }


    private void addFilm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film))
                .accept(MediaType.APPLICATION_JSON));
    }

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}