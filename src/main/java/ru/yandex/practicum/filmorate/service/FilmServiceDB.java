package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;

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











}
