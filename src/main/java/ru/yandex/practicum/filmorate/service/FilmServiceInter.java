package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmServiceInter {
    void addLike(Film film, User user);

    List<Film> getPopularFilm(int count);

    void deleteLike(Film film, User user);
}
