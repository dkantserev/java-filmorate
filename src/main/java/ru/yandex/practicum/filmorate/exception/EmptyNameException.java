package ru.yandex.practicum.filmorate.exception;

public class EmptyNameException extends MyFilmorateBadRequestException{
    public EmptyNameException(String message) {
        super(message);
    }
}
