package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendDB.FriendMemory;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserServiceInter {

    @Autowired
    InMemoryUserStorage userStorage;
    @Autowired
    FriendMemory friendMemory;

    @Override
    public List<User> mutualFriends(User main, User friend) {
        if (!friendMemory.getMap().containsKey(main) && !friendMemory.getMap().containsKey(friend)) {
            return new ArrayList<>();
        }
        List<User> mF = friendMemory.getMap().get(main).stream().
                filter(friendMemory.getMap().get(friend)::contains).collect(Collectors.toList());

        return mF;
    }

}
