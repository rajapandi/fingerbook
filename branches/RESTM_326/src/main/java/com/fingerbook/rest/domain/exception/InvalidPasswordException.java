package com.fingerbook.rest.domain.exception;

public class InvalidPasswordException extends ApplicationException {
	private static final long serialVersionUID = 1879296109055212487L;
	private String password;

    public InvalidPasswordException(String username) {
        this.password = username;
    }
    
    public String getUsername() {
        return password;
    }

}
