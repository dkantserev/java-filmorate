package ru.yandex.practicum.filmorate.validator;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.function.Predicate;

public interface FilmValidator extends Predicate<Film> {
    RuntimeException errorObject();
}
