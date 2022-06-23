package com.tech1.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(long id) {
        super("User with id '" + id + "' not found");
    }
}
