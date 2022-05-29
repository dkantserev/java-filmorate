package ru.yandex.practicum.filmorate.exception;



public abstract class MyFilmorateBadRequestException extends RuntimeException{
    public MyFilmorateBadRequestException(String message) {
        super(message);
    }
}
