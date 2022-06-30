package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    @Autowired
    private List<UserValidator> userValidatorList;

    private final Map<Integer, User> userMap = new HashMap();
    int id = 0;

    @Override
    public void add(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Optional<UserValidator> userValidator = userValidatorList.stream()
                .filter(validator -> !validator.test(user))
                .findFirst();
        userValidator.ifPresent(validator -> {
            throw validator.errorObject();
        });

        id++;
        user.setId(id);
        userMap.put(id, user);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void update(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        } else {
            throw new IllegalUpdateObject("There is no movie with this id");
        }
    }

    @Override
    public User getId(int id) {
        if (id < 0) {
            throw new NotFoundException("negative id" + id);
        }
        for (User value : userMap.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

    @Override
    public User delete(int id) {
        User u ;
        if(userMap.containsKey(id)){
            u=userMap.get(id);
            userMap.remove(id);
            return u;
        }
        else{
            throw new NotFoundException("user not found");
        }
    }



}
