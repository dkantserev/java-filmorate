package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void add(User user) {
        userStorage.add(user);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public void update(User user) {
        userStorage.update(user);
    }

    public User getId(int id) {
        return userStorage.getId(id);
    }

    public User delete(int id) {
        return userStorage.delete(id);
    }

    public int getIdUser(User user){
      return   userStorage.getIdUser(user);
    }
}
