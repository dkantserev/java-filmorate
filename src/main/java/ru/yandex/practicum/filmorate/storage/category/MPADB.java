package ru.yandex.practicum.filmorate.storage.category;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MPADB implements MPADBInter {
    private final JdbcTemplate jdbcTemplate;

    public MPADB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<MPA> getAllMPA(){

        List<ru.yandex.practicum.filmorate.model.MPA> f=jdbcTemplate.query("SELECT * FROM CATEGORY",
                new RowMapper<MPA>() {
            @Override
            public ru.yandex.practicum.filmorate.model.MPA mapRow(ResultSet rs, int rowNum) throws SQLException {
                ru.yandex.practicum.filmorate.model.MPA mpa = new MPA(rs.getInt(1),
                        rs.getString(2) );
                return mpa;
            }
        });
        return f;
    }

    @Override
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

    @Override
    public void updateMPA(Film film){
        jdbcTemplate.update("UPDATE FILMCATEGORY SET CATEGORY_ID=? WHERE FILM_ID=?",
                film.getMpa().getId(), film.getId());
    }
}
