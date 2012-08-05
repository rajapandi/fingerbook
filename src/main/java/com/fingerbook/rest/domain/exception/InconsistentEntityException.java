package com.fingerbook.rest.domain.exception;

import com.fingerbook.rest.domain.exception.ApplicationException;

public class InconsistentEntityException extends ApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String property;
    
    public InconsistentEntityException(String property) {
        this.property = property;
    } 
    
    public String getProperty() {
        return this.property;
    }
}
