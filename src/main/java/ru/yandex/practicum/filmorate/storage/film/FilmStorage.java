package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {
    void add(Film film);

    Map<Integer, Film> getAll();

    void update(Film film);

    List<Film> get();

    Film getId(int id);
}
