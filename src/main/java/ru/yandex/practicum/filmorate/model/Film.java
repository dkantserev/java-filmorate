package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private int id = 0;
    @Size(min = 1, max = 200)
    @NotNull
    @NotBlank
    private final String name;
    @Size(min = 6, max = 200)
    @NotNull
    @NotBlank
    private final String description;
    private final LocalDate releaseDate;
    @Min(1)
    private final int duration;

}
