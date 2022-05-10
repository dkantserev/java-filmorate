package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IllegalUpdateObject;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private List<UserValidator> filmValidatorList;
    private final Map<Integer, User> userMap = new HashMap();
    int id = 0;

    @GetMapping("/users")
    public Map<Integer, User> getUsers() {
        return userMap;
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) {

        Optional<UserValidator> filmValidator = filmValidatorList.stream()
                .filter(validator -> !validator.test(user))
                .findFirst();
        filmValidator.ifPresent(validator -> {
            throw validator.errorObject();
        });
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        id++;
        user.setId(id);
        userMap.put(id, user);
        log.info("add user" + user);
        return user;
    }

    @PutMapping("/users")
    public User updateFilm(@RequestBody User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
        } else {
            throw new IllegalUpdateObject("There is no movie with this id");
        }
        log.info("update user" + user);
        return user;
    }
}
