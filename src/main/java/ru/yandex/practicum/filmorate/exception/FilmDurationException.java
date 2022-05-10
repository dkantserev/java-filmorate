package ru.yandex.practicum.filmorate.exception;

public class FilmDurationException extends MyFilmorateBadRequestException{
    public FilmDurationException(String message) {
        super(message);
    }
}
