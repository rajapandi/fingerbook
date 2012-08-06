package com.fingerbook;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findMyClassesByCodeEquals" })
public class MyClass {

    @NotNull
    private String message;

    @NotNull
    @Size(min = 1, max = 30)
    private String code;
}
