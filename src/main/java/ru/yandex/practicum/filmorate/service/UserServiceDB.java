package ru.yandex.practicum.filmorate.service;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Service
public class UserServiceDB implements UserServiceInter {

    private final JdbcTemplate jdbcTemplate;


    public UserServiceDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public List<User> mutualFriends(User main, User friend) {

        String sql = String.format("SELECT * FROM USERBASE WHERE USER_ID IN(SELECT FRIEND_ID FROM FRIENDLIST" +
                " WHERE  USER_ID=%s or USER_ID=%s  GROUP BY FRIEND_ID HAVING FRIEND_ID<>%s and FRIEND_ID<>%s)",
                main.getId(), friend.getId(), main.getId(), friend.getId());
        return jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User(rs.getString("EMAIL"), rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate());
                u.setId(rs.getInt(1));
                u.setName(rs.getString("NAME"));
                return u;
            }
        });
    }

    public int getDD(User user) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT USER_ID FROM USERBASE WHERE NAME=?", user.getName());
        return rowSet.getInt(1);
    }
}
