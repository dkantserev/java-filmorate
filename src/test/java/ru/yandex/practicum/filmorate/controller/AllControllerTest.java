package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.category.MPADB;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.friendDB.FriendDB;
import ru.yandex.practicum.filmorate.storage.genre.GenreDB;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/shema.sql", "/data-test.sql"})
class AllControllerTest {
    @Autowired
    FilmDbStorage filmDbStorage;
    @Autowired
    UserDbStorage userDbStorage;

    @Autowired
    FriendDB friendDB;
    @Autowired
    ObjectMapper mapper;

    @Autowired
    GenreDB genreDB;
    @Autowired
    MPADB mpadb;

    @Autowired
    MockMvc mockMvc;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {

        User user = new User("gjjjl@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void test2_getUserWithValidIdSAndDelete() throws Exception {

        User user = new User("gjjjl@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users/").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(userDbStorage.getAll().size(), 1);
        this.mockMvc.perform(delete("/users/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(userDbStorage.getAll().size(), 0);
    }

    @Test
    void test3_putGetUserWithNegativId() throws Exception {

        User user = new User("gjjjl@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users/").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        user.setId(-1);
        String body1 = mapper.writeValueAsString(user);
        this.mockMvc.perform(put("/users/").content(body1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        this.mockMvc.perform(get("/users/-1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        this.mockMvc.perform(delete("/users/-1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
        assertEquals(userDbStorage.getAll().size(), 1);
        this.mockMvc.perform(delete("/users/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void test4_friend() throws Exception {

        User user = new User("gjjjl@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        User user2 = new User("gujjjl@hh.tr", "gfffgf@hh.tr", LocalDate.of(1955, 2,
                2));
        user2.setName("hhhffh");
        User user3 = new User("gjjffjl@hh.tr", "gfssgf@hh.tr", LocalDate.of(1956, 2,
                2));
        user3.setName("hhddhh");
        String body = mapper.writeValueAsString(user);
        String body2 = mapper.writeValueAsString(user2);
        String body3 = mapper.writeValueAsString(user3);
        this.mockMvc.perform(post("/users/").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        this.mockMvc.perform(post("/users/").content(body2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        this.mockMvc.perform(post("/users/").content(body3).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/users/")).andExpect(status().is(200));
        assertEquals(userDbStorage.getAll().size(), 3);
        this.mockMvc.perform(put("/users/1/friends/2")).andExpect(status().is(200));
        this.mockMvc.perform(put("/users/2/friends/1")).andExpect(status().is(200));
        this.mockMvc.perform(put("/users/3/friends/2")).andExpect(status().is(200));
        this.mockMvc.perform(put("/users/3/friends/1")).andExpect(status().is(200));
        this.mockMvc.perform(put("/users/1/friends/3")).andExpect(status().is(200));
        this.mockMvc.perform(put("/users/2/friends/3")).andExpect(status().is(200));
        this.mockMvc.perform(get("/users/1/friends")).andExpect(status().is(200));
        assertEquals(friendDB.getAllFriends(userDbStorage.getId(1)).size(), 2);
        assertEquals(friendDB.mutualFriends(userDbStorage.getId(1), userDbStorage.getId(2)).size(), 1);
        this.mockMvc.perform(delete("/users/1/friends/2")).andExpect(status().is(200));
        assertEquals(friendDB.getAllFriends(userDbStorage.getId(1)).size(), 1);

    }

    @Test
    void test5_MPA() throws Exception {

        this.mockMvc.perform(get("/mpa")).andExpect(status().is(200));
        this.mockMvc.perform(get("/mpa/1")).andExpect(status().is(200));
        assertEquals(mpadb.getMpaId(1).getName(), "G");
    }

    @Test
    void test6_Genres() throws Exception {

        this.mockMvc.perform(get("/genres")).andExpect(status().is(200));
        this.mockMvc.perform(get("/genres/1")).andExpect(status().is(200));
        assertEquals(genreDB.getGenresId(1).getName(), "Комедия");
    }

    @Test
    void test6_Films() throws Exception {

        Film film = new Film("Кинконг", " что то про обезьяну", LocalDate.of(1933, 1,
                1), 60);
        Film film2 = new Film("Кинконг", " что то про обезьяну", LocalDate.of(1961, 1,
                1), 60);
        film.setMpa(new MPA(1, null));
        film2.setMpa(new MPA(3, null));
        User user = new User("gjjjl@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        String bodyU = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users/").content(bodyU).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        String body = mapper.writeValueAsString(film);
        String body2 = mapper.writeValueAsString(film2);
        this.mockMvc.perform(post("/films/").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        this.mockMvc.perform(put("/films/").content(body2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        this.mockMvc.perform(put("/films/1/like/1").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
        assertEquals(filmDbStorage.getPopularFilm(1).size(), 1);
        this.mockMvc.perform(delete("/films/1/like/1")).andExpect(status().is(200));
        this.mockMvc.perform(get("/films/1")).andExpect(status().is(200));


    }


}