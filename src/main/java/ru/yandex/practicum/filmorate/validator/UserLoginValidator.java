package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserLoginException;
import ru.yandex.practicum.filmorate.model.User;

@Service
@Order(25)
public class UserLoginValidator implements UserValidator {
    @Override
    public RuntimeException errorObject() {
        return new UserLoginException("void login");
    }

    @Override
    public boolean test(User user) {
        return !user.getLogin().isBlank();
    }
}
