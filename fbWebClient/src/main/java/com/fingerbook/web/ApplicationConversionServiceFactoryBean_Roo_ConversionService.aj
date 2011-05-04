// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.fingerbook.web;

import com.fingerbook.MyClass;
import java.lang.String;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(new MyClassConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
    static class com.fingerbook.web.ApplicationConversionServiceFactoryBean.MyClassConverter implements Converter<MyClass, String>  {
        public String convert(MyClass myClass) {
        return new StringBuilder().append(myClass.getMessage()).append(" ").append(myClass.getCode()).toString();
        }
        
    }
    
}
