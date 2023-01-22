package com.samsara.paladin.exceptions.user;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String errorMessage) {
        super(errorMessage);
    }
}
