package com.fingerbook.web;

import com.fingerbook.MyClass;
import org.springframework.roo.addon.web.mvc.controller.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RooWebScaffold(path = "myclasses", formBackingObject = MyClass.class)
@RequestMapping("/myclasses")
@Controller
public class MyClassController {
}
