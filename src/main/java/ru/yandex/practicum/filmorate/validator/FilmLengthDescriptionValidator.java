package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LengthDescriptionFilmException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
@Order(10)
public class FilmLengthDescriptionValidator implements FilmValidator {
    @Override
    public RuntimeException errorObject() {
        return new LengthDescriptionFilmException("large length description film");
    }

    @Override
    public boolean test(Film film) {
        return film.getDescription().length()<=200;
    }
}
