package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.util.List;

public interface GenreDBInter {
    List<Genres> getAllGenres();

    Genres getGenresId(int id);

    void updateGenre(Film film, int film_id);

    void addGenre(Film film, int film_id);
}
