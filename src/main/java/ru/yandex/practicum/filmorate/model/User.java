package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private int id = 0;
    @Size(min = 6, max = 200)
    @NotNull
    @NotBlank
    private final String email;
    @Size(min = 6, max = 200)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private final String login;
    @Size(min = 3, max = 200)
    private String name;
    @Past
    private final LocalDate birthday;

}
