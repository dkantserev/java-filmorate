package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesMemory;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService implements FilmServiceInter {

    @Autowired
    LikesMemory likesMemory;
    @Autowired
    InMemoryFilmStorage filmStorage;

    @Override
    public List<Film> getPopularFilm(int count) {
        List<Film> r = likesMemory.getLikeMap().entrySet().stream().
                sorted((Comparator.comparingInt(o -> o.getValue().size())))
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


}
