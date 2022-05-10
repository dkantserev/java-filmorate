package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserBirthdayException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;

@Service
@Order(35)
public class UserBirthdayValidator implements UserValidator{
    @Override
    public RuntimeException errorObject() {
        return new UserBirthdayException("date of birth cannot be in the future");
    }

    @Override
    public boolean test(User user) {
        return user.getBirthday().isBefore(LocalDate.now());
    }
}
