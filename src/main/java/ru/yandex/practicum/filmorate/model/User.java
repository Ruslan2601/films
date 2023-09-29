package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;

@Data
public class User {

    private int id;

    @Email
    private String email;

    @NotEmpty(message = "логин не может быть пустым")
    @NotBlank(message = "логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
