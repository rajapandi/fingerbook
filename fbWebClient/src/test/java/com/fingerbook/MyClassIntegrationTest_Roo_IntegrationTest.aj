// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.fingerbook;

import com.fingerbook.MyClassDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect MyClassIntegrationTest_Roo_IntegrationTest {
    
    declare @type: MyClassIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: MyClassIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: MyClassIntegrationTest: @Transactional;
    
    @Autowired
    private MyClassDataOnDemand MyClassIntegrationTest.dod;
    
    @Test
    public void MyClassIntegrationTest.testCountMyClasses() {
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", dod.getRandomMyClass());
        long count = com.fingerbook.MyClass.countMyClasses();
        org.junit.Assert.assertTrue("Counter for 'MyClass' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void MyClassIntegrationTest.testFindMyClass() {
        com.fingerbook.MyClass obj = dod.getRandomMyClass();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to provide an identifier", id);
        obj = com.fingerbook.MyClass.findMyClass(id);
        org.junit.Assert.assertNotNull("Find method for 'MyClass' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'MyClass' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void MyClassIntegrationTest.testFindAllMyClasses() {
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", dod.getRandomMyClass());
        long count = com.fingerbook.MyClass.countMyClasses();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'MyClass', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<com.fingerbook.MyClass> result = com.fingerbook.MyClass.findAllMyClasses();
        org.junit.Assert.assertNotNull("Find all method for 'MyClass' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'MyClass' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void MyClassIntegrationTest.testFindMyClassEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", dod.getRandomMyClass());
        long count = com.fingerbook.MyClass.countMyClasses();
        if (count > 20) count = 20;
        java.util.List<com.fingerbook.MyClass> result = com.fingerbook.MyClass.findMyClassEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'MyClass' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'MyClass' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void MyClassIntegrationTest.testFlush() {
        com.fingerbook.MyClass obj = dod.getRandomMyClass();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to provide an identifier", id);
        obj = com.fingerbook.MyClass.findMyClass(id);
        org.junit.Assert.assertNotNull("Find method for 'MyClass' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyMyClass(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'MyClass' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MyClassIntegrationTest.testMerge() {
        com.fingerbook.MyClass obj = dod.getRandomMyClass();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to provide an identifier", id);
        obj = com.fingerbook.MyClass.findMyClass(id);
        boolean modified =  dod.modifyMyClass(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        com.fingerbook.MyClass merged = (com.fingerbook.MyClass) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'MyClass' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void MyClassIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", dod.getRandomMyClass());
        com.fingerbook.MyClass obj = dod.getNewTransientMyClass(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'MyClass' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'MyClass' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void MyClassIntegrationTest.testRemove() {
        com.fingerbook.MyClass obj = dod.getRandomMyClass();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'MyClass' failed to provide an identifier", id);
        obj = com.fingerbook.MyClass.findMyClass(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'MyClass' with identifier '" + id + "'", com.fingerbook.MyClass.findMyClass(id));
    }
    
}
