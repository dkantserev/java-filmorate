package ru.yandex.practicum.filmorate.exception;

public class UserBirthdayException extends MyFilmorateBadRequestException{
    public UserBirthdayException(String message) {
        super(message);
    }
}
