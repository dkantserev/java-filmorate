package ru.yandex.practicum.filmorate.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.*;


@org.springframework.web.bind.annotation.ControllerAdvice

public class ControllerAdvice {
    @ExceptionHandler(NullContextException.class)
    public ResponseEntity<String> handleNullContext(NullContextException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalUpdateObject.class)
    public ResponseEntity<String> handleIllegalUpdateObject(IllegalUpdateObject e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataReleaseException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(DataReleaseException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyNameException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(EmptyNameException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FilmDurationException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(FilmDurationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LengthDescriptionFilmException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(LengthDescriptionFilmException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBirthdayException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(UserBirthdayException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(UserLoginException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadEmailException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(BadEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleIllegalUpdateObject(NotFoundException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

}
