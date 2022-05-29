package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EmptyNameException;
import ru.yandex.practicum.filmorate.model.Film;

@Service
@Order(20)
public class FilmNameIsBlankValidator implements FilmValidator {
    @Override
    public RuntimeException errorObject() {
        return new EmptyNameException("void name");
    }

    @Override
    public boolean test(Film film) {
        return !film.getName().isBlank();
    }
}
