package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.friendDB.FriendInt;

import java.util.List;
import java.util.Map;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void add(Film film) {
        filmStorage.add(film);
    }

    public Map<Integer, Film> getAll() {
        return filmStorage.getAll();
    }

    public void update(Film film) {
        filmStorage.update(film);
    }

    public List<Film> get() {
        return filmStorage.get();
    }

    public Film getId(int id) {
        return filmStorage.getId(id);
    }

    public void delete(int id) {
        filmStorage.delete(id);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getPopularFilm(count);
    }

    public int getId(Film film){
        return filmStorage.getId(film);
    }
}
