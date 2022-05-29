package ru.yandex.practicum.filmorate.validator;
import ru.yandex.practicum.filmorate.model.User;
import java.util.function.Predicate;

public interface UserValidator extends Predicate<User> {
    RuntimeException errorObject();
}
