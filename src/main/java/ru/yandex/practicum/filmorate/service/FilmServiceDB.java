package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class FilmServiceDB implements FilmServiceInter {

    private final JdbcTemplate jdbcTemplate;

    public FilmServiceDB(JdbcTemplate jdbcTemplate) {
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
    public List<Film> getPopularFilm(int count) {

        String sql = String.format("SELECT * FROM FILMBASE LEFT JOIN FILMCATEGORY F on" +
                " FILMBASE.FILM_ID = F.FILM_ID LEFT JOIN CATEGORY C on F.CATEGORY_ID = C.CATEGORY_ID" +
                " ORDER BY LIKES desc LIMIT %s", count);
        List<Film> f = jdbcTemplate.query(sql, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film(rs.getString(2), rs.getString(3),
                        rs.getDate(4).toLocalDate(), rs.getInt(5));
                film.setId(rs.getInt(1));
                film.setLike(rs.getInt(6));
                film.setMpa(new MPA(rs.getInt(9), rs.getString(10)));
                return film;
            }

        });
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT FILMBASE.FILM_ID," +
                " GENRE.NAME FROM FILMBASE LEFT JOIN  \"FILMGENRE\" FG on FILMBASE.FILM_ID = FG.\"FILM_ID\" " +
                "LEFT JOIN \"GENRE\" ON FG.\"GENRE_ID\" = \"GENRE\".\"GENRE_ID\" ");
        Map<Integer, Set<Genres>> g = new HashMap<>();
        while (rowSet.next()) {
            if (g.containsKey(rowSet.getInt(1))) {
                g.get(rowSet.getInt(1)).add(new Genres(rowSet.getInt(1),
                        rowSet.getString(2)));
            } else {
                Set<Genres> s = new HashSet<>();
                s.add(new Genres(rowSet.getInt(1), rowSet.getString(2)));
                g.put(rowSet.getInt(1), s);
            }
        }
        for (Film film : f) {
            for (Integer integer : g.keySet()) {
                if (film.getId() == integer) {
                    film.setGenres(g.get(integer));
                }
            }
        }

        return f;
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

    public MPA getMpaId(int id) {

        if (id < 0) {
            throw new NotFoundException("negativ id");
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet( "SELECT * FROM CATEGORY WHERE CATEGORY_ID=?",id);
        if(sqlRowSet.next()){
            return new MPA(sqlRowSet.getInt(1),sqlRowSet.getString(2));
        }
    return null;
    }

    public List<MPA> getAllMPA(){

        List<MPA> f=jdbcTemplate.query("SELECT * FROM CATEGORY", new RowMapper<MPA>() {
            @Override
            public MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
                MPA mpa = new MPA(rs.getInt(1), rs.getString(2) );
                return mpa;
            }
        });
        return f;
    }

    public List<Genres> getAllGenres() {

        List<Genres> f=jdbcTemplate.query("SELECT * FROM GENRE ORDER BY GENRE_ID  ", new RowMapper<Genres>() {
            @Override
            public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genres genres = new Genres(rs.getInt(1), rs.getString(2) );
                return genres;
            }
        });
        return f;
    }

    public Genres getGenresId(int id) {
        if (id < 0) {
            throw new NotFoundException("negativ id");
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet( "SELECT * FROM GENRE WHERE GENRE_ID=?",id);
        if(sqlRowSet.next()){
            return new Genres(sqlRowSet.getInt(1),sqlRowSet.getString(2));
        }
        return null;
    }
}
