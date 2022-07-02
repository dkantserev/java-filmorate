package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

@Component
public class LikesDB implements LikesInt {
    private final JdbcTemplate jdbcTemplate;

    public LikesDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Film film, User user) {
        jdbcTemplate.update("INSERT INTO LIKELIST (USER_ID,FILM_ID) VALUES ( ?,? ) ", user.getId(), film.getId());
        SqlRowSet s = jdbcTemplate.queryForRowSet("SELECT COUNT(USER_ID) FROM LIKELIST WHERE FILM_ID=? ",
                film.getId());
        if (s.next()) {
            int like= s.getInt(1);
            jdbcTemplate.update("UPDATE FILMBASE SET LIKES=? WHERE FILM_ID=?", like, film.getId());
        }
    }

    @Override
    public void deleteLike(Film film, User user) {

        jdbcTemplate.update("DELETE FROM LIKELIST WHERE USER_ID=?",user.getId());
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT LIKES FROM FILMBASE WHERE FILM_ID=?",film.getId());
        if(sqlRowSet.next()) {
            int like = sqlRowSet.getInt(1);
            jdbcTemplate.update("UPDATE FILMBASE SET LIKES=? WHERE FILM_ID=?" , like-1,film.getId());
        }

    }
}
