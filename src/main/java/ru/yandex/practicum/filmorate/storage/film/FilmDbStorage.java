package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NullContextException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.category.MPADB;
import ru.yandex.practicum.filmorate.storage.genre.GenreDB;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    List<FilmValidator> filmValidatorList;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    GenreDB db;
    @Autowired
    MPADB mpadb;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, List<FilmValidator> filmValidatorList) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidatorList = filmValidatorList;
    }

    @Override
    public void add(Film film) {

        if (film.getMpa() == null) {
            throw new NullContextException("mpa not be null");
        }
        Optional<FilmValidator> filmValidator = filmValidatorList.stream()
                .filter(validator -> !validator.test(film))
                .findFirst();
        filmValidator.ifPresent(validator -> {
            throw validator.errorObject();
        });
        if (film.getId() < 0) {
            throw new NotFoundException("negativ id");
        }
        int id = 0;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT MAX(FILM_ID) FROM FILMBASE");
        if (rowSet.next()) {
            id = rowSet.getInt(1) + 1;
        } else {
            id = 1;
        }

        jdbcTemplate.update("INSERT INTO FILMBASE (FILM_ID,NAME, DESCRIPTION, RELEASEDATE, DURATION,LIKES)" +
                        " VALUES(?,?,?,?,?,?)", id,
                film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getLike());
        SqlRowSet s = jdbcTemplate.queryForRowSet("SELECT FILM_ID from FILMBASE where NAME=?", film.getName());
        if (s.next()) {
            int film_id = s.getInt(1);
            if (film.getMpa() != null) {
                jdbcTemplate.update("INSERT INTO FILMCATEGORY(FILM_ID, CATEGORY_ID) VALUES ( ?,? )",
                        film_id, film.getMpa().getId());
            }
            if (film.getGenres() != null) {
                db.addGenre(film, film_id);
            }
        }
    }

    @Override
    public Map<Integer, Film> getAll() {
        return null;
    }

    @Override
    public void update(Film film) {

        if (film.getId() < 0) {
            throw new NotFoundException("negativ id");
        }
        jdbcTemplate.update("UPDATE FILMBASE SET NAME=?,DESCRIPTION=?,RELEASEDATE=?,DURATION=?,LIKES=?" +
                        " WHERE FILM_ID=? ", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getLike(), film.getId());
        mpadb.updateMPA(film);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILMGENRE WHERE FILM_ID=?", film.getId());
        if (sqlRowSet.next()) {
            if (film.getGenres().isEmpty()) {
                jdbcTemplate.update("DELETE FROM FILMGENRE WHERE FILM_ID=?", film.getId());
            }
            if (film.getGenres() != null) {
                db.updateGenre(film, film.getId());
                db.addGenre(film, film.getId());
            }
        } else if (film.getGenres() != null) {
            db.addGenre(film, film.getId());
        }
    }

    @Override
    public List<Film> get() {

        List<Film> f = jdbcTemplate.query("SELECT * FROM FILMBASE LEFT JOIN FILMCATEGORY F" +
                        " on FILMBASE.FILM_ID = F.FILM_ID LEFT JOIN CATEGORY C on F.CATEGORY_ID = C.CATEGORY_ID",
                new RowMapper<>() {
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
                "LEFT JOIN \"GENRE\" ON FG.\"GENRE_ID\" = \"GENRE\".\"GENRE_ID\" ORDER BY  GENRE.GENRE_ID desc ");
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
    public Film getId(int id) {

        if (id < 0) {
            throw new NotFoundException("negativ id");
        }
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FILMBASE " +
                "LEFT JOIN FILMCATEGORY F on FILMBASE.FILM_ID = F.FILM_ID  " +
                "LEFT JOIN CATEGORY C on F.CATEGORY_ID = C.CATEGORY_ID WHERE FILMBASE.FILM_ID=?", id);
        if (rowSet.next()) {
            Film j = new Film(rowSet.getString(2), rowSet.getString(3),
                    rowSet.getDate(4).toLocalDate(), rowSet.getInt(5));
            j.setId(rowSet.getInt(1));
            j.setLike(rowSet.getInt(6));
            j.setMpa(new MPA(rowSet.getInt(9), rowSet.getString(10)));
            SqlRowSet crutch = jdbcTemplate.queryForRowSet("SELECT GENRE.GENRE_ID," +
                    " GENRE.NAME FROM FILMBASE LEFT JOIN  \"FILMGENRE\" FG on FILMBASE.FILM_ID = FG.\"FILM_ID\" " +
                    "LEFT JOIN \"GENRE\" ON FG.\"GENRE_ID\" = \"GENRE\".\"GENRE_ID\" WHERE FILMBASE.FILM_ID=? " +
                    "ORDER BY GENRE.GENRE_ID desc", id);
            Set<Genres> g = new HashSet<>();
            int h = 0;
            if (crutch.next()) {
                h = crutch.getInt(1);
            }
            SqlRowSet ggrowSet = jdbcTemplate.queryForRowSet("SELECT GENRE.GENRE_ID," +
                    " GENRE.NAME FROM FILMBASE LEFT JOIN  \"FILMGENRE\" FG on FILMBASE.FILM_ID = FG.\"FILM_ID\" " +
                    "LEFT JOIN \"GENRE\" ON FG.\"GENRE_ID\" = \"GENRE\".\"GENRE_ID\" WHERE FILMBASE.FILM_ID=? " +
                    "ORDER BY GENRE.GENRE_ID desc", id);
            if (h != 0) {
                while (ggrowSet.next()) {
                    g.add(new Genres(ggrowSet.getInt(1), ggrowSet.getString(2)));
                }
                j.setGenres(g);
            }
            return j;
        }
        return null;
    }

    @Override
    public void delete(int id) {

        Film film = getId(id);
        jdbcTemplate.update("DELETE FROM FILMCATEGORY WHERE FILM_ID=?", id);
        jdbcTemplate.update("DELETE FROM FILMGENRE WHERE FILM_ID=?", id);
        jdbcTemplate.update("DELETE FROM FILMBASE WHERE FILM_ID=?", id);
    }

    public int getId(Film film) {

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT FILM_ID FROM FILMBASE WHERE NAME=?", film.getName());
        int id = 0;
        if (rowSet.next()) {
            id = rowSet.getInt(1);
        }
        return id;
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
