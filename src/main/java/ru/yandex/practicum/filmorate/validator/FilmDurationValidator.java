package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmDurationException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
@Order(1)
public class FilmDurationValidator implements FilmValidator {
    @Override
    public RuntimeException errorObject() {
        return new FilmDurationException("movie duration must be positive");
    }

    @Override
    public boolean test(Film film) {
        return film.getDuration()>0;
    }
}
