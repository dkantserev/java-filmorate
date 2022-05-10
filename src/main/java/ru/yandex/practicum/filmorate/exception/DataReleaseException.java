package ru.yandex.practicum.filmorate.exception;

public class DataReleaseException extends MyFilmorateBadRequestException{
    public DataReleaseException(String message) {
        super(message);
    }
}
