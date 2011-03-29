package com.fingerbook.rest.domain.exception;

public class InvalidPasswordException extends ApplicationException {

    private String password;

    public InvalidPasswordException(String username) {
        this.password = password;
    }
    
    public String getUsername() {
        return password;
    }

}
