package ru.yandex.practicum.filmorate.storage.friendDB;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FriendDB implements FriendInt {
    private final JdbcTemplate jdbcTemplate;

    public FriendDB(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(User main, User friend) {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDLIST WHERE USER_ID=? and FRIEND_ID=?"
                , main.getId(), friend.getId());
        SqlRowSet check = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDLIST WHERE USER_ID=? and FRIEND_ID=?",
                friend.getId(), main.getId());
        if (sqlRowSet.next()) {
            if (sqlRowSet.getInt(3) == 3) {
                throw new IllegalUpdateObject("it's friend");
            }
        }
        if (check.next()) {
            if (check.getInt(3) == 2) {
                jdbcTemplate.update("INSERT INTO FRIENDLIST (USER_ID, FRIEND_ID, STATUS_ID) VALUES ( ?,?,? )",
                        main.getId(), friend.getId(), 3);
                jdbcTemplate.update("UPDATE FRIENDLIST SET STATUS_ID=3 WHERE USER_ID=? AND FRIEND_ID=?",
                        friend.getId(), main.getId());
            }
        } else {
            jdbcTemplate.update("INSERT INTO FRIENDLIST (USER_ID, FRIEND_ID, STATUS_ID) VALUES ( ?,?,? )",
                    main.getId(), friend.getId(), 2);
        }
    }

    @Override
    public Set<User> getAllFriends(User user) {

        List<User> f = new ArrayList<>();
        String g = String.format("SELECT * FROM FRIENDLIST LEFT JOIN USERBASE U on FRIENDLIST.FRIEND_ID = U.USER_ID" +
                " where FRIENDLIST.USER_ID=%s", user.getId());
        f = jdbcTemplate.query(g, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User(rs.getString("EMAIL"), rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate());
                u.setId(rs.getInt(4));
                u.setName(rs.getString("NAME"));
                return u;
            }
        });
        Set<User> h = new HashSet<>(f);
        return h;
    }

    @Override
    public void deleteFriend(User main, User friend) {

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM FRIENDLIST WHERE USER_ID=? and FRIEND_ID=?",
                main.getId(), friend.getId());
        if (sqlRowSet.next()) {
            jdbcTemplate.update("DELETE FROM FRIENDLIST  WHERE USER_ID=? and FRIEND_ID=?",
                    main.getId(), friend.getId());
        }
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
}
