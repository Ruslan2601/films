package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    @NotEmpty(message = "название не может быть пустым")
    private String name;

    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @MinimumDate
    private LocalDate releaseDate;

    @Positive(message = "продолжительность фильма должна быть положительной")
    private long duration;
}
