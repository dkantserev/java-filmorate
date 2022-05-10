package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.exception.NullContextException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class FilmController {
    @Autowired
    List<FilmValidator> filmValidatorList;
    private final Map<Integer, Film> filmMap = new HashMap();
    private int id = 0;

    @GetMapping("/films")
    public Map<Integer, Film> getFilms() {
        return filmMap;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {
        if (film.getName() == null || film.getReleaseDate() == null || film.getDescription().isBlank()) {
            throw new NullContextException("object contains null parameters");
        } else {
            Optional<FilmValidator> filmValidator = filmValidatorList.stream()
                    .filter(validator -> !validator.test(film))
                    .findFirst();
            filmValidator.ifPresent(validator -> {
                throw validator.errorObject();
            });
            id++;
            film.setId(id);
            filmMap.put(id, film);
        }
        log.info("add film" + film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (filmMap.containsKey(film.getId())) {
            if (film.getDescription() == null || film.getName() == null || film.getReleaseDate() == null) {
                throw new NullContextException("object contains null parameters");
            } else {
                Optional<FilmValidator> filmValidator = filmValidatorList.stream()
                        .filter(validator -> !validator.test(film))
                        .findFirst();
                filmValidator.ifPresent(validator -> {
                    throw validator.errorObject();
                });
                filmMap.put(film.getId(), film);
            }
        } else {
            throw new IllegalUpdateObject("There is no movie with this id");
        }
        log.info("update film" + film);
        return film;
    }


}
