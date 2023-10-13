package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.yandex.practicum.filmorate.util.MinimumDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Integer> likes = new HashSet<>();

    @Positive(message = "продолжительность фильма должна быть положительной")
    private long duration;

    public void addLike(int id) {
        likes.add(id);
    }

    public void removeLike(int id) {
        likes.remove(id);
    }
}
