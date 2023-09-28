package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
