// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.fingerbook;

import java.lang.String;

privileged aspect MyClass_Roo_JavaBean {
    
    public String MyClass.getMessage() {
        return this.message;
    }
    
    public void MyClass.setMessage(String message) {
        this.message = message;
    }
    
    public String MyClass.getCode() {
        return this.code;
    }
    
    public void MyClass.setCode(String code) {
        this.code = code;
    }
    
}
