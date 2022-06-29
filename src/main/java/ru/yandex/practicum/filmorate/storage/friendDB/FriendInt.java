package ru.yandex.practicum.filmorate.storage.friendDB;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendInt {
    void addFriend(User main, User friend);

    Set<User> getAllFriends(User user);

    void deleteFriend(User main, User friend);
}
