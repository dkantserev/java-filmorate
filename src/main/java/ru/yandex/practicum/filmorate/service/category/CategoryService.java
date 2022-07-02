package ru.yandex.practicum.filmorate.service.category;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.category.MPADBInter;

import java.util.List;

@Service
public class CategoryService {
    private final MPADBInter mpadb;

    public CategoryService(@Qualifier("MPADB") MPADBInter mpadb) {
        this.mpadb = mpadb;
    }

    public MPA getMpaId(int id) {
        return mpadb.getMpaId(id);
    }

    public void updateMPA(Film film) {
        mpadb.updateMPA(film);
    }

    public List<MPA> getAllMPA(){
       return mpadb.getAllMPA();
    }
}
