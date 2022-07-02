package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.category.CategoryService;
import ru.yandex.practicum.filmorate.storage.category.MPADB;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/mpa")
@Validated
public class MPAController {

    @Autowired
    CategoryService mpa;

    @GetMapping()
    public List<MPA> allMPA() {
        return mpa.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA MPAGetId(@PathVariable int id) {
        return mpa.getMpaId(id);
    }
}
