package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserServiceInter {


    List<User> mutualFriends(User main, User friend);
}
