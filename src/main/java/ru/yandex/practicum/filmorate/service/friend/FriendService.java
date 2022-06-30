package ru.yandex.practicum.filmorate.service.friend;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendDB.FriendInt;

import java.util.List;
import java.util.Set;

@Service
public class FriendService {
    private final FriendInt friendInt;

    public FriendService(@Qualifier("friendDB") FriendInt friendInt) {
        this.friendInt = friendInt;
    }

    public Set<User> getAllFriends(User user) {
        return friendInt.getAllFriends(user);
    }

    public void deleteFriend(User main, User friend) {
        friendInt.deleteFriend(main, friend);
    }

    public List<User> mutualFriends(User main, User friend) {
        return friendInt.mutualFriends(main, friend);
    }

    public void addFriend(User main, User friend) {
        friendInt.addFriend(main, friend);
    }
}
