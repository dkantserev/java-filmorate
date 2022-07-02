package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql("/shema.sql")
class FilmDbStorageTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FilmDbStorage filmDbStorage;

    @Test
    void add() {

        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION)" +
                        " VALUES(?,?,?,?,?) ", 1, "namename", "Dessssss"
                , LocalDate.of(1950, 1, 1), 90);
        assertEquals(filmDbStorage.getId(1).getName(), "namename");
    }

    @Test
    void getAll() {

        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION)" +
                        " VALUES(?,?,?,?,?) ", 1, "namename", "Dessssss"
                , LocalDate.of(1950, 1, 1), 90);
        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION) VALUES(?,?,?,?,?) ", 2, "namen44ame", "Dess44ssss"
                , LocalDate.of(1950, 1, 1), 90);
        assertEquals(filmDbStorage.get().size(), 2);
    }

    @Test
    void update() {

        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION)" +
                        " VALUES(?,?,?,?,?) ", 1, "namename", "Dessssss"
                , LocalDate.of(1950, 1, 1), 90);
        jdbcTemplate.update("UPDATE FILMBASE SET name=? WHERE FILM_ID=1", "ffffffff");
        assertEquals(filmDbStorage.getId(1).getName(), "ffffffff");
    }


    @Test
    void delete() {

        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID, NAME, DESCRIPTION, RELEASEDATE, DURATION)" +
                        " VALUES(?,?,?,?,?) ", 1, "namename", "Dessssss"
                , LocalDate.of(1950, 1, 1), 90);
        jdbcTemplate.update("DELETE FROM FILMBASE WHERE FILM_ID=1");
        assertEquals(filmDbStorage.get().size(), 0);
    }


}