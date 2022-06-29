package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class LikesMemory implements LikesInt{

    @Autowired
    InMemoryFilmStorage filmStorage;
    @Autowired
    InMemoryUserStorage userStorage;

    public Map<Film, Set<User>> getLikeMap() {
        return likeMap;
    }

    public void setLikeMap(Map<Film, Set<User>> likeMap) {
        this.likeMap = likeMap;
    }

    private Map<Film, Set<User>> likeMap = new HashMap<>();

    @Override
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

    @Override
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
