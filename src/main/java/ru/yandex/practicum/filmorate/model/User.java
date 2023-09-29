package ru.yandex.practicum.filmorate.model;


import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    private int id;

    @Email
    private String email;

    @NotEmpty(message = "логин не может быть пустым")
    @NotNull
    @NotBlank(message = "логин не может содержать пробелы")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
