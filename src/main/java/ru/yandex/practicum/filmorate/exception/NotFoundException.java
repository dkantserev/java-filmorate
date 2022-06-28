package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends MyFilmorateBadRequestException{
    public NotFoundException(String message) {
        super(message);
    }
}
