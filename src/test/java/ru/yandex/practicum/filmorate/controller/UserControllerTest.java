package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    private final User user = new User(1, "Nick@Name2", "login", "Nick",
            LocalDate.of(2011, 10, 22));

    @Test
    public void testAddUser_LoginEmpty() throws Exception {
        user.setLogin(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("login - логин не может содержать пробелы или быть пустым;login - логин не может содержать пробелы или быть пустым;"));
    }

    @Test
    public void testAddUser_LoginWithSpace() throws Exception {
        user.setLogin("   ");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("login - логин не может содержать пробелы или быть пустым;"));
    }

    @Test
    public void testAddUser_NameEmpty() throws Exception {
        user.setName(null);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name")
                        .value("login"));
    }

    @Test
    public void testAddUser_BirthdayInFuture() throws Exception {
        user.setBirthday(LocalDate.now().plusDays(2));
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("birthday - дата рождения не может быть в будущем;"));
    }

    @Test
    public void testAddUser_WrongEmail() throws Exception {
        user.setEmail("mail.ru");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email - электронная почта должна содержать символ @;"));
    }

    @Test
    public void testAddUser_EmptyEmail() throws Exception {
        user.setEmail("");
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("email - email не может быть пустым;"));
    }

    @Test
    public void getUser_Size() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.length()").value(0));

        addUser();

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void getUser_Equals() throws Exception {
        addUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(List.of(user))));
    }

    @Test
    public void testAddUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Nick"))
                .andExpect(jsonPath("$.email").value("Nick@Name2"))
                .andExpect(jsonPath("$.birthday").value("2011-10-22"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        addUser();
        user.setName("Max");
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.email").value("Nick@Name2"))
                .andExpect(jsonPath("$.birthday").value("2011-10-22"));
    }

    private void addUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user))
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