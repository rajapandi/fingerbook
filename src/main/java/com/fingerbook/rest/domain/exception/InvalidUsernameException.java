package com.fingerbook.rest.domain.exception;

public class InvalidUsernameException extends ApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String username;

    public InvalidUsernameException(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }

}
