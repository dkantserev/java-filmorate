package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesDB;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;


import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    @Autowired
    FilmDbStorage storage;
    @Autowired
    UserDbStorage userStorage;
    @Autowired
    LikesDB likesDB;


    @GetMapping
    public List<Film> get() {
        return storage.get();
    }

    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        storage.add(film);
        log.info("add film" + film);
        int id = storage.getId(film);
        film.setId(id);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        storage.update(film);
        log.info("update film" + film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        likesDB.addLike(storage.getId(id), userStorage.getId(userId));
        return storage.getId(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        likesDB.deleteLike(storage.getId(id), userStorage.getId(userId));
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        return storage.getPopularFilm(count);
    }

    @GetMapping("/{id}")
    public Film getId(@PathVariable int id) {
        return storage.getId(id);
    }




}
