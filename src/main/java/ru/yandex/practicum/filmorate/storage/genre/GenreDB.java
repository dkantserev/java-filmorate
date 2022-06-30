package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GenreDB implements GenreDBInter {
    private final JdbcTemplate jdbcTemplate;

    public GenreDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
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

    @Override
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
    @Override
    public void updateGenre(Film film, int film_id){
        film.getGenres().stream().mapToInt(Genres::getId).
                forEach(o -> jdbcTemplate.update(("UPDATE  FILMGENRE SET GENRE_ID=?" +
                        " WHERE FILM_ID=? ORDER BY GENRE_ID desc "), o, film_id));
    }
    @Override
    public void addGenre(Film film, int film_id){
        film.getGenres().stream().mapToInt(Genres::getId).
                forEach(o -> jdbcTemplate.update(("INSERT INTO  FILMGENRE (GENRE_ID, FILM_ID) " +
                        "VALUES ( ?,? )"), o, film_id));
    }
}
