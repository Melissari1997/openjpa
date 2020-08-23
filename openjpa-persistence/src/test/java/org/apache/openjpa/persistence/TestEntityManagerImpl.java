package org.apache.openjpa.persistence;

import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.openjpa.kernel.Bootstrap;
import org.apache.openjpa.kernel.BrokerFactory;
import org.apache.openjpa.slice.SingleEMFTestCase;
import org.junit.Test;


public class TestEntityManagerImpl extends SingleEMFTestCase{
	
    public void setUp() throws Exception {
		EntityManagerFactoryImpl emf = new EntityManagerFactoryImpl();
    }
	    @Test
	    public void testEMConfig001() {
	    	BrokerFactory bf0 = Bootstrap.getBrokerFactory();
	    	EntityManagerFactoryImpl emf = new EntityManagerFactoryImpl(bf0);
	    	
	        Map propMap = new HashMap();
	        EntityManager em = emf.createEntityManager(propMap);
	    
	        //EntityManagerImpl eml = (EntityManagerImpl) em;
	        
	    }


}
