package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;


public interface UserStorage {

    void add(User user);

    List<User> getAll();

    void update(User user);

    User getId(int id);

    void delete(int id);

}
