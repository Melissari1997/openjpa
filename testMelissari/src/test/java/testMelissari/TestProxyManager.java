package testMelissari;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.lib.log.Log;
import org.apache.openjpa.util.ProxyManagerImpl;
import org.junit.Before;

/**
 * Test proxies generated by the proxy manager.
 *
 * @author Paolo Melissari
 */

@RunWith(Enclosed.class)
public class TestProxyManager {
	private static ProxyManagerImpl _proxyManager = new ProxyManagerImpl();
    
	@RunWith(Parameterized.class)
	public static class TestCopyMap{
		private static Map<Integer, String> emptyMap = new HashMap<Integer, String>();
		private static Map<Integer, String> notEmptyMap = new HashMap<Integer, String>();
		private Map<Integer, String> inputMap;
		private Map<Integer, String> expectedMapResult;
		
		@Before
	    public void setup() {
	        notEmptyMap.put(new Integer(1), "1");
	    }
		
		@Parameterized.Parameters
	    public static Collection<Object[]> ClassLoaderParameters() {
	    	Map orig = (Map) _proxyManager.newMapProxy(HashMap.class, null, null, null, false);
	    	return Arrays.asList(new Object[][] {
	    		//Test suite minimale
	    		{null, null},
	    		{emptyMap, emptyMap},
	    		{notEmptyMap, notEmptyMap},
	    		//adeguacy
	    		{new HashMap(orig), (Map) _proxyManager.copyMap(orig)},
	    		});
	    };
	    public TestCopyMap(Map<Integer, String> inputMap, Map<Integer, String> expectedMapResult) {
	    	this.inputMap = inputMap;
	    	this.expectedMapResult = expectedMapResult;
	    }
	    @Test
	    public void testCopyMaps() {
	    	Map actualMapResult = _proxyManager.copyMap(inputMap);
	        assertMapsEquals(expectedMapResult,actualMapResult );
	    }
	    
	    /**
	     * Assert that the given maps are exactly the same.
	     */
	    private static void assertMapsEquals(Map m1, Map m2) {
	    	
	    	if(m1 == null) {
	        		assertNull(m2);
	        		return;
	        }
	    	assertTrue(m1.getClass() == m2.getClass()); //stesso tipo
	        assertEquals(m1.size(), m2.size()); //stessa grandezza
	        assertEquals(m1, m2); // stesso contenuto
	    }
	  }
	
	
	
	
	
	
	@RunWith(Parameterized.class)
	public static class TestCopyArray{
		private static Integer[] emptyArray;
		private static Integer[] notEmptyArray = new Integer[1];
		private Object  inputArray;
		private Integer[]  expectedArray;

		@Before
	    public void setup() {
			notEmptyArray[0] = new Integer(1);
	    }
		
		@Parameterized.Parameters
	    public static Collection<Object[]> ClassLoaderParameters() {
	    	return Arrays.asList(new Object[][] {
	    		//Test suite minimale
	    		{null, null},
	    		{emptyArray, emptyArray},
	    		{notEmptyArray, notEmptyArray},
	    		
	    		//adeguacy
	    		{"", null},
	    		});
	    };
	    public TestCopyArray(Object inputArray, Integer[]  expectedArray) {
	    	this.inputArray = inputArray;
	    	this.expectedArray = expectedArray;
	    }
	    @Test
	    public void testCopyArrays() {
	    	Object actualArrayResult;
	    	try{
	    		 actualArrayResult =  (Integer[]) _proxyManager.copyArray(inputArray);
	  
	    		assertArraysEquals(expectedArray, actualArrayResult);
	    	}catch(Exception e) {
				if(!(inputArray instanceof String)) {
					fail("Input type is not an Array and is not a String");
				}
	    	}
	    }
	    
	    /**
	     * Assert that the given arrays are exactly the same.
	     * 
	     */
	    private void assertArraysEquals(Integer[]  array1, Object array2) {
	    	if(array1 == null) {
	        		assertNull(array2);
	        		return;
	    	}
	    	assertTrue(array1.getClass() == array2.getClass()); //stesso tipo
	        assertEquals(Array.getLength(array1), Array.getLength(array2)); //stessa grandezza
	        Integer[] newarray2 = new Integer[Array.getLength(array2)];
	        System.arraycopy(array2, 0, newarray2, 0, 1);
	        assertTrue(Arrays.equals(array1, newarray2)); // stesso contenuto
	    }
	  }
	
	public static class TestCopyCustom{
		private Date date;
		private CustomDate customDate;
		private TreeMap map;
		private TreeMap customComparatorMap;
		private CustomObject emptyFoo = new CustomObject();
		private CustomObject copyFoo;
		
		@Before
	    public void setup() {
			this.date = new Date();
			long currentDate = 10;
			this.customDate = new CustomDate(currentDate);
			this.map = new TreeMap();
			map.put(new Integer(1), "1");
			map.put(new Integer(2), "2");
			this.customComparatorMap = new TreeMap(new CustomComparator());
			customComparatorMap.put(new Integer(1), "test");
			customComparatorMap.put(new Integer(2), "test2");
	    }

	    @Test
	    public void testCopyCustomDate() {
	    	Object actualDateResult =  _proxyManager.copyCustom(this.date);
	        assertDateEquals(this.date,actualDateResult);
	        
	        actualDateResult = _proxyManager.copyCustom(this.customDate);
	        assertDateEquals(this.customDate,actualDateResult);
	    }
	    
	    /**
	     * Assert that the given date are exactly the same.
	     */
	    private void assertDateEquals(Date date1, Object date2) {
	    	
	    	if(date1 == null) {
	        		assertNull(date2);
	        		return;
	        }
	    	assertTrue(date1.getClass() == date2.getClass()); //stesso tipo
	        assertEquals(date1.compareTo((Date)date2), 0); //stessa data
	    }
	    
	    
	    @Test
	    public void testCopyCustomMap() {
	    	Object actualMapResult =  _proxyManager.copyCustom(this.map);
	    	assertMapsEqual(this.map,actualMapResult);
	        
	    	actualMapResult = _proxyManager.copyCustom(this.customComparatorMap);
	        assertMapsEqual(this.customComparatorMap,actualMapResult);
	    }
	    
	    /**
	     * Assert that the given maps are exactly the same.
	     */
	    private void assertMapsEqual(Map map1, Object map2) {
	    	if(map1 == null) {
	    		assertNull(map2);
	    		return;
	    	}
	        assertTrue(map1.getClass() == map2.getClass());
	        assertEquals(map1.size(), ((TreeMap)map2).size());
	        assertEquals(map1, ((TreeMap)map2));
	    }
	    
	    @Test
	    public void testCopyCustomObject() {
	    	Object actualFooResult =  _proxyManager.copyCustom(this.emptyFoo);
	    	assertCustomObjectEqual(this.emptyFoo,actualFooResult);
	    	//assertNotNull(this.copyFoo);
	    	this.emptyFoo.setNumber(1);
	    	this.emptyFoo.setString("Changed");
	    	this.copyFoo = new CustomObject(this.emptyFoo);
	    	actualFooResult = _proxyManager.copyCustom(this.copyFoo);
	    	assertCustomObjectEqual(this.copyFoo,actualFooResult);
	    }
	    
	    /**
	     * Assert that the given beans are exactly the same.
	     */
	    private void assertCustomObjectEqual(CustomObject b1, Object b2) {
	    	if(b1 == null) {
	    		assertNull(b2);
	    		return;
	    	}
	        assertTrue(b1.getClass() == b2.getClass());
	        assertTrue(b1.getString() ==  ((CustomObject) b2).getString());
	        assertTrue(b1.getNumber() ==  ((CustomObject) b2).getNumber());
	    }
	    
	    @Test
	    public void testCopyCustomNullObject() {
	    	Object actualFooResult =  _proxyManager.copyCustom(null);
	    	assertNull(actualFooResult);
	    }
	    
	    @Test
	    public void testCopyCustomProxy() {
	    	CustomObject orig =  (CustomObject)_proxyManager.newCustomProxy(new CustomObject(),true);
	    	CustomObject comp = new CustomObject();
	    	assertCustomObjectEqual(comp, (CustomObject)_proxyManager.copyCustom(orig) );
	    }
	    
	    @Test
	    public void testCopyCalendar() {
	    	Calendar orig = new GregorianCalendar();
	    	Calendar actualCalendarResult = (Calendar) _proxyManager.copyCustom(orig);
	    	assertEquals(orig.getClass(),actualCalendarResult.getClass() );
	    	assertEquals(orig,actualCalendarResult);
	    }
	    
	    @Test
	    public void testCopyCustomCollection() {
	    	List orig = new ArrayList();
	    	List actualCollectionResult = (List) _proxyManager.copyCustom(orig);
	    	assertEquals(orig.getClass(),actualCollectionResult.getClass() );
	    	assertEquals(orig,actualCollectionResult);
	    }
	    
	    
	    
	    @Test
	    public void testNotProxyable() {
	    	final String orig = new String();
	    	String actualStringResult = (String) _proxyManager.copyCustom(orig);
	    	assertNull(actualStringResult);
	    }
	    
	    
	    public static class CustomDate extends Date {
	        public CustomDate(long time) {
	            super(time);
	        }
	    }
	    
	    public static  class CustomObject {
	        private String _string;
	        private int _number;
	        public CustomObject() {
	        	this._string = "";
	        	this._number = -1;
	        }
	        public CustomObject(CustomObject bean) {
	            setString(bean.getString());
	            setNumber(bean.getNumber());
	        }
	        public String getString() {
	            return _string;
	        }

	        public void setString(String str) {
	            _string = str;
	        }

	        public int getNumber() {
	            return _number;
	        }

	        public void setNumber(int number) {
	            _number = number;
	        }
	    }

	    private static  class CustomComparator implements Comparator {

	        @Override
	        public int compare(Object o1, Object o2) {
	            return ((Comparable) o1).compareTo(o2)*-1;
	        }
	    }
	  }
}
