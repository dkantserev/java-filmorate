package ru.yandex.practicum.filmorate.service.like;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.likes.LikesInt;

@Service
public class LikeService {
    private final LikesInt likesInt;

    public LikeService(@Qualifier("likesDB") LikesInt likesInt) {
        this.likesInt = likesInt;
    }

    public void addLike(Film film, User user) {
        likesInt.addLike(film, user);
    }

    public void deleteLike(Film film, User user) {
        likesInt.deleteLike(film, user);
    }
}
