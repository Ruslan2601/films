package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmsGenre {

    @NonNull
    private int filmId;

    @NonNull
    private int genreId;

    @NonNull
    private String name;

}
