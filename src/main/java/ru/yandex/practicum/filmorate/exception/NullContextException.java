package ru.yandex.practicum.filmorate.exception;

public class NullContextException extends MyFilmorateBadRequestException{
    public NullContextException(String message) {
        super(message);
    }
}
