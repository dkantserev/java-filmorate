package ru.yandex.practicum.filmorate.exception;


public class IllegalUpdateObject extends MyFilmorateBadRequestException{
    public IllegalUpdateObject(String message) {
        super(message);
    }
}
