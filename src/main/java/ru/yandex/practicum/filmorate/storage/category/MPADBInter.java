package ru.yandex.practicum.filmorate.storage.category;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface MPADBInter {
    List<MPA> getAllMPA();

    MPA getMpaId(int id);

    void updateMPA(Film film);
}
