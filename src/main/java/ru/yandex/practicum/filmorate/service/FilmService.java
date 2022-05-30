package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {
    @Autowired
    InMemoryFilmStorage filmStorage;
    @Autowired
    InMemoryUserStorage userStorage;

    private Map<Film, Set<User>> likeMap = new HashMap<>();

    public void addLike(Film film, User user) {
        if (filmStorage.getAll().containsValue(film) && userStorage.getAll().contains(user)) {
            if (!likeMap.containsKey(film)) {
                Set<User> g = new HashSet<>();
                g.add(user);
                likeMap.put(film, g);
            } else {
                likeMap.get(film).add(user);
            }
        } else {
            throw new NotFoundException("user or film don't found");
        }
    }

    public List<Film> getPopularFilm(int count) {
        List<Film> r = likeMap.entrySet().stream().sorted((Comparator.comparingInt(o -> o.getValue().size())))
                .map(Map.Entry::getKey).limit(count).collect(Collectors.toList());
        if (r.size() < count && r.size() < filmStorage.getAll().size()) {
            for (Film value : filmStorage.getAll().values()) {
                if (!r.contains(value)) {
                    r.add(value);
                }
            }
            return filmStorage.getAll().values().stream().limit(count - r.size()).collect(Collectors.toList());
        }
        return r;

    }

    public void deleteLike(Film film, User user) {
        if (!likeMap.containsKey(film)) {
            throw new NotFoundException("film don't found");
        }
        if (!likeMap.get(film).contains(user)) {
            throw new NotFoundException("the user didn't like the movie");
        } else {
            likeMap.get(film).remove(user);
        }
    }


}
