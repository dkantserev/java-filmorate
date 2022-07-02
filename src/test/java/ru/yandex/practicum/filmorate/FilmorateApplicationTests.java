package ru.yandex.practicum.filmorate;


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
import java.time.LocalDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/shema.sql","/data-test.sql"})
class FilmorateApplicationTests {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void test1_createValidUserResponseShouldBeOk() throws Exception {

        User user = new User("124gjjjl@hh.tr", "gf5342gf@hh.tr",
                LocalDate.of(2001, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_UserDateOfBirthInTheFuture() throws Exception {

        User user = new User("fjj@h.tr", "gfgf@hh.tr", LocalDate.of(2500, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test3_UserLoginVoid() throws Exception {

        User user = new User("gfgf@hh.tr", "", LocalDate.of(2500, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test4_UserBadEmail() throws Exception {

        User user = new User("Ёпрст@hh.tr", "", LocalDate.of(2500, 2, 2));
        user.setName("hhhh");
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test5_createValidFilmResponseShouldBeOk() throws Exception {

        Film film = new Film("gfgf@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2
                , 2), 90);
        film.setMpa(new MPA(1,null));
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test6_FilmDurationNegative() throws Exception {

        Film film = new Film("gfgf@hh.tr", "gfgf@hh.tr", LocalDate.of(2001, 2
                , 2), -90);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test7_FilmLengthDescription200() throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <200; i++) {
            stringBuilder.append("r");
        }
        Film film = new Film("gfgf@hh.tr", stringBuilder.toString(), LocalDate.of(2001, 2
                , 2), 90);
        film.setMpa(new MPA(1,null));
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    void test7_FilmLengthDescription201() throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 200; i++) {
            stringBuilder.append("r");
        }
        Film film = new Film("gfgf@hh.tr", stringBuilder.toString(), LocalDate.of(2001, 2
                , 2), 90);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test8_FilmNameIsBlank() throws Exception {

        Film film = new Film("  ", "hggh", LocalDate.of(2001, 2, 2), 90);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void test8_veryOldFilm1894() throws Exception {

        Film film = new Film("  ", "hggh", LocalDate.of(1894, 2, 2), 90);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }
}


