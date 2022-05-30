package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    InMemoryUserStorage userStorage;
    private Map<User, Set<User>> greatFriendsMap = new HashMap<>();

    public Set<User> getAllFriends(User user) {
        if (greatFriendsMap.isEmpty()) {
            throw new NotFoundException("friends don't found");
        }
        return greatFriendsMap.get(user);
    }

    public void addFriend(User main, User friend) {

        if (userStorage.getAll().contains(main) && userStorage.getAll().contains(friend)) {
            if (greatFriendsMap.containsKey(main)) {
                greatFriendsMap.get(main).add(friend);
            } else {
                Set<User> j = new HashSet<>();
                j.add(friend);
                greatFriendsMap.put(main, j);
            }
            if (greatFriendsMap.containsKey(friend)) {
                greatFriendsMap.get(friend).add(main);
            } else {
                Set<User> j = new HashSet<>();
                j.add(main);
                greatFriendsMap.put(friend, j);
            }

        } else {
            throw new NotFoundException("user or friend don't found");
        }
    }

    public void deleteFriend(User main, User friend) {

        if (greatFriendsMap.containsKey(main) && greatFriendsMap.containsKey(friend)) {
            greatFriendsMap.get(main).remove(friend);
        } else {
            throw new NotFoundException("user or friend don't found");
        }
    }

    public List<User> mutualFriends(User main, User friend) {
        if (!greatFriendsMap.containsKey(main) && !greatFriendsMap.containsKey(friend)) {
            return new ArrayList<>();
        }
        List<User> mF = greatFriendsMap.get(main).stream().
                filter(greatFriendsMap.get(friend)::contains).collect(Collectors.toList());

        return mF;
    }

}