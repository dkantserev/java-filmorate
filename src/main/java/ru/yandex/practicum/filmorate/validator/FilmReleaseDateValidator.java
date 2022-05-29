package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataReleaseException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

@Service
@Order(30)
public class FilmReleaseDateValidator implements FilmValidator{
    @Override
    public RuntimeException errorObject() {
        return new DataReleaseException("release date must be later than December 28, 1985");
    }

    @Override
    public boolean test(Film film) {
        return film.getReleaseDate().isAfter(LocalDate.of(1895,12,28)) ;
    }
}
