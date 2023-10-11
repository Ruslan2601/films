package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;

    @NotEmpty(message = "email не может быть пустым")
    @Email(message = "электронная почта должна содержать символ @")
    private String email;

    @NotEmpty(message = "логин не может содержать пробелы или быть пустым")
    @NotBlank(message = "логин не может содержать пробелы или быть пустым")
    private String login;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }

}
