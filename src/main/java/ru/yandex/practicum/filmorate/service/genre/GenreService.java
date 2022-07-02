package ru.yandex.practicum.filmorate.service.genre;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBInter;

import java.util.List;

@Service
public class GenreService {
    private final GenreDBInter genreDBInter;

    public GenreService(@Qualifier("genreDB") GenreDBInter genreDBInter) {
        this.genreDBInter = genreDBInter;
    }

    public List<Genres> getAllGenres() {
        return genreDBInter.getAllGenres();
    }

    public Genres getGenresId(int id) {
        return genreDBInter.getGenresId(id);
    }

    public void updateGenre(Film film, int film_id) {
        genreDBInter.updateGenre(film, film_id);
    }

    public void addGenre(Film film, int film_id) {
        genreDBInter.addGenre(film, film_id);
    }
}
