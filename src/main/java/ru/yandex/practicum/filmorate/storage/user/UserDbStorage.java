package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(User user) {

        if (user.getId() < 0) {
            throw new NotFoundException("negativ id");
        }
        int id = 0;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT MAX(USER_ID) FROM USERBASE");
        if (rowSet.next()) {
            id = rowSet.getInt(1) + 1;
        } else {
            id = 1;
        }
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update("INSERT INTO USERBASE(USER_ID,email,login, name,birthday) VALUES(?,?,?,?,?)", id,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public List<User> getAll() {

        return jdbcTemplate.query("SELECT * FROM USERBASE", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User(rs.getString(2), rs.getString(3),
                        rs.getDate(5).toLocalDate());
                u.setId(rs.getInt(1));
                u.setName(rs.getString(4));
                return u;
            }
        });
    }

    @Override
    public void update(User user) {

        if (user.getId() < 0) {
            throw new NotFoundException("negativ id");
        }
        jdbcTemplate.update("UPDATE USERBASE SET EMAIL =?,  NAME =?, BIRTHDAY =? , LOGIN =? WHERE USER_ID=?",
                user.getEmail(), user.getName(), user.getBirthday(), user.getLogin(), user.getId());
    }

    @Override
    public User getId(int id) {

        if (id < 0) {
            throw new NotFoundException("negativ id");
        }
        SqlRowSet set = jdbcTemplate.queryForRowSet("SELECT * FROM UserBase WHERE user_id =?", id);
        if (set.next()) {
            User user = new User(
                    set.getString("email"),
                    set.getString("login"),
                    set.getDate("birthday").toLocalDate()
            );
            user.setName(set.getString("name"));
            user.setId(id);
            return user;
        }
        return null;
    }

    @Override
    public User delete(int id) {
        User u = getId(id);
        jdbcTemplate.update("DELETE FROM USERBASE WHERE USER_ID=?", id);
        return u;
    }

    public int getIdUser(User user) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERBASE WHERE LOGIN=?",
                user.getLogin());
        int id = 0;
        if (rowSet.next()) {
            id = rowSet.getInt(1);
        }
        return id;
    }

    public int getDD(User user) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERBASE WHERE NAME=?", user.getName());
        return rowSet.getInt(1);
    }
}
