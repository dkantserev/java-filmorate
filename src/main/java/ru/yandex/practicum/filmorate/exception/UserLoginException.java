package ru.yandex.practicum.filmorate.exception;

public class UserLoginException extends MyFilmorateBadRequestException{
    public UserLoginException(String message) {
        super(message);
    }
}
