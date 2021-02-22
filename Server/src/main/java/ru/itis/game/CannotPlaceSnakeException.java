package ru.itis.game;

public class CannotPlaceSnakeException extends Exception{
    public CannotPlaceSnakeException() {
    }

    public CannotPlaceSnakeException(String message) {
        super(message);
    }

    public CannotPlaceSnakeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotPlaceSnakeException(Throwable cause) {
        super(cause);
    }
}
