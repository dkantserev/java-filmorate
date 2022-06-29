package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserServiceInter {
    Set<User> getAllFriends(User user);

    void addFriend(User main, User friend);

    void deleteFriend(User main, User friend);

    List<User> mutualFriends(User main, User friend);
}
