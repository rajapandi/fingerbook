package com.fingerbook.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fingerbook.MyClass;

@RequestMapping("/publicmyclass/**")
@Controller
@SessionAttributes("myclass")
public class PublicMyClassController {

    @RequestMapping(method = RequestMethod.GET)
    public String get(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    	modelMap.put("myclass", getMyClass());
    	return "publicmyclass";
    }

    @RequestMapping(method = RequestMethod.POST, value = "{id}")
    public void post(@PathVariable Long id, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) {
    }

    @RequestMapping
    public String index() {
        return "publicmyclass/index";
    }
    
   	private MyClass getMyClass() {
	    MyClass myClass = new MyClass();
			try {
		    	String code = SecurityContextHolder.getContext().getAuthentication().getName();
		    	myClass.setCode(code);
				// Cast due to http://java.sun.com/javaee/5/docs/api/javax/persistence/Query.html#getSingleResult()
				myClass = (MyClass) MyClass.findMyClassesByCodeEquals(code).getSingleResult();
			} catch (DataAccessException ignored) { /* no MyClass for this code was found, so start a new MyClass */ }
		return myClass;
   	}

}
