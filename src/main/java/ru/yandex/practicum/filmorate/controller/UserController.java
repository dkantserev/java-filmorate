package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    InMemoryUserStorage storage;
    @Autowired
    UserService userService;

    @GetMapping
    public List<User> get() {
        return storage.getAll();
    }

    @PostMapping
    public User add(@Valid @RequestBody User user) {
        storage.add(user);
        log.info("add user" + user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        storage.update(user);
        log.info("update user" + user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(storage.getId(id), storage.getId(friendId));
        return storage.getId(id);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable @NotNull Map<String, String> patchV) {
        if (!patchV.get("id").isBlank() && !patchV.get("friendId").isBlank()) {
            userService.deleteFriend(storage.getId(Integer.parseInt(patchV.get("id"))),
                    storage.getId(Integer.parseInt(patchV.get("friendId"))));
            return storage.getId(Integer.parseInt(patchV.get("friendId")));
        }
        return null;
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable int id) {
        return userService.getAllFriends(storage.getId(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutual(@PathVariable int id, @PathVariable int otherId) {
        return userService.mutualFriends(storage.getId(id), storage.getId(otherId));
    }

    @GetMapping("/{id}")
    public User getId(@PathVariable int id) {
        return storage.getId(id);
    }


}
