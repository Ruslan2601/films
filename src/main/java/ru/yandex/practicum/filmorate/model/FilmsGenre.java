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
    private int film_id;

    @NonNull
    private int genre_id;

    @NonNull
    private String name;

}
