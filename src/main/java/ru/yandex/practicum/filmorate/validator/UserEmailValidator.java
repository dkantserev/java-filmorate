package ru.yandex.practicum.filmorate.validator;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadEmailException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Order(15)
public class UserEmailValidator implements UserValidator{

    @Override
    public RuntimeException errorObject() {
        return new BadEmailException("illegal format email");
    }

    @Override
    public boolean test(User user) {
        if(user.getName().isBlank()){
            return false;
        }
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\" +
                ".[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(user.getEmail());
        return m.matches();
    }
}
