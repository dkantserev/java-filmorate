package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id = 0;
    @Size(min = 1, max = 200)
    @NotBlank
    private final String name;
    @Size(min = 6, max = 200)
    @NotBlank
    private final String description;
    private final LocalDate releaseDate;
    @Min(1)
    private final int duration;
    private Set<Genres> genres ;
    private MPA mpa;
    private int like;

}
