package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullContextException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.likes.LikesMemory;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class InMemoryFilmStorage implements FilmStorage {

    List<FilmValidator> filmValidatorList;

    LikesMemory likesMemory;

    public List<FilmValidator> getFilmValidatorList() {
        return filmValidatorList;
    }

    public void setFilmValidatorList(List<FilmValidator> filmValidatorList) {
        this.filmValidatorList = filmValidatorList;
    }

    public LikesMemory getLikesMemory() {
        return likesMemory;
    }

    public void setLikesMemory(LikesMemory likesMemory) {
        this.likesMemory = likesMemory;
    }

    private final Map<Integer, Film> filmMap = new HashMap();
    private int id = 0;

    @Override
    public void add(Film film) {
        if (film.getName() == null || film.getReleaseDate() == null || film.getDescription().isBlank()) {
            throw new NullContextException("object contains null parameters");
        } else {
            Optional<FilmValidator> filmValidator = filmValidatorList.stream()
                    .filter(validator -> !validator.test(film))
                    .findFirst();
            filmValidator.ifPresent(validator -> {
                throw validator.errorObject();
            });
            id++;
            film.setId(id);
            filmMap.put(id, film);
        }
    }

    @Override
    public Map<Integer, Film> getAll() {
        return filmMap;
    }

    @Override
    public List<Film> get() {
        return new ArrayList<>(filmMap.values());
    }

    @Override
    public void update(Film film) {
        if (filmMap.containsKey(film.getId())) {
            if (film.getDescription() == null || film.getName() == null || film.getReleaseDate() == null) {
                throw new NullContextException("object contains null parameters");
            } else {
                Optional<FilmValidator> filmValidator = filmValidatorList.stream()
                        .filter(validator -> !validator.test(film))
                        .findFirst();
                filmValidator.ifPresent(validator -> {
                    throw validator.errorObject();
                });
                filmMap.put(film.getId(), film);
            }
        } else {
            throw new IllegalUpdateObject("There is no movie with this id");
        }
    }

    @Override
    public Film getId(int id) {
        if (id < 0) {
            throw new NotFoundException("negative id" + id);
        }
        for (Film value : filmMap.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

    @Override
    public void delete(int id) {
        if (filmMap.containsKey(id)) {
            filmMap.remove(id);
        } else {
            throw new NotFoundException("film not found");
        }
    }

    public List<Film> getPopularFilm(int count) {
        List<Film> r = likesMemory.getLikeMap().entrySet().stream().
                sorted((Comparator.comparingInt(o -> o.getValue().size())))
                .map(Map.Entry::getKey).limit(count).collect(Collectors.toList());
        if (r.size() < count && r.size() < getAll().size()) {
            for (Film value : getAll().values()) {
                if (!r.contains(value)) {
                    r.add(value);
                }
            }
            return getAll().values().stream().limit(count - r.size()).collect(Collectors.toList());
        }
        return r;

    }

    @Override
    public int getId(Film film) {
        return filmMap.get(film.getId()).getId();
    }
}


