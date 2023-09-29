package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {
    @Mock
    private BindingResult bindingResult;

    private final UserController userController = new UserController();

    private final User user = new User(1, "Nick@Name2", "login", "Nick",
            LocalDate.of(2011, 10, 22));

    @Test
    public void getUser_Size() {
        assertEquals(0, userController.getFilms().size(), "Список не пуст");
        userController.userMap.put(user.getId(), user);
        assertEquals(1, userController.getFilms().size(), "Неверный размер списка");
    }

    @Test
    public void addUser_Equals() {
        userController.addUser(user, bindingResult);
        assertEquals(user, userController.userMap.get(user.getId()), "Объекты неравны");
    }

    @Test
    public void updateUser_Equals() {
        userController.addUser(user, bindingResult);
        user.setName("Max");
        userController.updateUser(user, bindingResult);
        assertEquals(user, userController.userMap.get(user.getId()), "Объекты неравны");
    }

}