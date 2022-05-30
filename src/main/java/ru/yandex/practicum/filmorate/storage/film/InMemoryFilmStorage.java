package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullContextException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;


@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Autowired
    List<FilmValidator> filmValidatorList;
    private final Map<Integer, Film> filmMap = new HashMap();
    private int id = 0;

    @Override
    public void add(Film film) {
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
    }

    @Override
    public Map<Integer, Film> getAll() {
        return filmMap;
    }

    @Override
    public List<Film> get() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public void update(Film film) {
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
    }

    @Override
    public Film getId(int id) {
        if (id < 0) {
            throw new NotFoundException("negative id" + id);
        }
        for (Film value : filmMap.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

}
