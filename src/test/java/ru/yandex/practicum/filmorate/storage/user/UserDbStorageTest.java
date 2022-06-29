package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql("/shema.sql")
class UserDbStorageTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    UserDbStorage userDbStorage;

    @Test
    void add() {

        jdbcTemplate.update("INSERT INTO USERBASE (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)" +
                        " VALUES ( ?,?,?,?,? ) ", 1, "email@email"
                , "loginlogin", "name", "1980-1-1");
        assertEquals(userDbStorage.getAll().size(), 1);

    }

    @Test
    void getAll() {

        jdbcTemplate.update("INSERT INTO USERBASE (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)" +
                        " VALUES ( ?,?,?,?,? ) ", 1, "email@email"
                , "loginlogin", "name", "1980-1-1");
        jdbcTemplate.update("INSERT INTO USERBASE (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                        "VALUES ( ?,?,?,?,? ) ", 2, "ema44il@email"
                , "logi55nlogin", "na66me", "1981-1-1");
        assertEquals(userDbStorage.getAll().size(), 2);

    }

    @Test
    void update() {

        jdbcTemplate.update("INSERT INTO USERBASE (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)" +
                        " VALUES ( ?,?,?,?,? ) ", 1, "email@email"
                , "loginlogin", "name", "1980-1-1");
        jdbcTemplate.update("UPDATE USERBASE SET NAME=? WHERE USER_ID=1 ", "hhhhhhhh");
        SqlRowSet s = jdbcTemplate.queryForRowSet("SELECT NAME FROM USERBASE WHERE USER_ID=1");
        String ss = "";
        if (s.next()) {
            ss = s.getString(1);
        }
        assertEquals(ss, "hhhhhhhh");

    }

    @Test
    void getId() {

        jdbcTemplate.update("INSERT INTO USERBASE (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY) " +
                        "VALUES ( ?,?,?,?,? ) ", 1, "email@email"
                , "loginlogin", "name", "1980-1-1");
        SqlRowSet s = jdbcTemplate.queryForRowSet("SELECT NAME FROM USERBASE WHERE USER_ID=1");
        String ss = "";
        if (s.next()) {
            ss = s.getString(1);
        }
        assertEquals(ss, "name");
        jdbcTemplate.update("DELETE FROM USERBASE WHERE USER_ID=1");
    }


}