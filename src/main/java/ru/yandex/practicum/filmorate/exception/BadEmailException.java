package ru.yandex.practicum.filmorate.exception;

public class BadEmailException extends MyFilmorateBadRequestException{
    public BadEmailException(String message) {
        super(message);
    }
}
