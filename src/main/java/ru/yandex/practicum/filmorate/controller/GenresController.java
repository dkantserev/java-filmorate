package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.storage.genre.GenreDB;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@Validated
public class GenresController {

    @Autowired
    GenreService genreDB;


    @GetMapping()
    public List<Genres> allMPA() {
        return genreDB.getAllGenres() ;
    }

    @GetMapping("/{id}")
    public Genres MPAGetId(@PathVariable int id) {
        return genreDB.getGenresId(id);
    }
}
