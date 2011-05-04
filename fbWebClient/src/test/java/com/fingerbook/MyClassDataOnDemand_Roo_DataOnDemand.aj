// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.fingerbook;

import com.fingerbook.MyClass;
import java.util.List;
import java.util.Random;
import org.springframework.stereotype.Component;

privileged aspect MyClassDataOnDemand_Roo_DataOnDemand {
    
    declare @type: MyClassDataOnDemand: @Component;
    
    private Random MyClassDataOnDemand.rnd = new java.security.SecureRandom();
    
    private List<MyClass> MyClassDataOnDemand.data;
    
    public MyClass MyClassDataOnDemand.getNewTransientMyClass(int index) {
        com.fingerbook.MyClass obj = new com.fingerbook.MyClass();
        setMessage(obj, index);
        setCode(obj, index);
        return obj;
    }
    
    private void MyClassDataOnDemand.setMessage(MyClass obj, int index) {
        java.lang.String message = "message_" + index;
        obj.setMessage(message);
    }
    
    private void MyClassDataOnDemand.setCode(MyClass obj, int index) {
        java.lang.String code = "code_" + index;
        if (code.length() > 30) {
            code = code.substring(0, 30);
        }
        obj.setCode(code);
    }
    
    public MyClass MyClassDataOnDemand.getSpecificMyClass(int index) {
        init();
        if (index < 0) index = 0;
        if (index > (data.size() - 1)) index = data.size() - 1;
        MyClass obj = data.get(index);
        return MyClass.findMyClass(obj.getId());
    }
    
    public MyClass MyClassDataOnDemand.getRandomMyClass() {
        init();
        MyClass obj = data.get(rnd.nextInt(data.size()));
        return MyClass.findMyClass(obj.getId());
    }
    
    public boolean MyClassDataOnDemand.modifyMyClass(MyClass obj) {
        return false;
    }
    
    public void MyClassDataOnDemand.init() {
        data = com.fingerbook.MyClass.findMyClassEntries(0, 10);
        if (data == null) throw new IllegalStateException("Find entries implementation for 'MyClass' illegally returned null");
        if (!data.isEmpty()) {
            return;
        }
        
        data = new java.util.ArrayList<com.fingerbook.MyClass>();
        for (int i = 0; i < 10; i++) {
            com.fingerbook.MyClass obj = getNewTransientMyClass(i);
            obj.persist();
            obj.flush();
            data.add(obj);
        }
    }
    
}
