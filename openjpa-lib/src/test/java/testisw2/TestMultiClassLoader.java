package testisw2;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;


/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Collection;

import org.apache.openjpa.lib.util.MultiClassLoader;
import org.junit.Before;

/**
 * Tests the {@link MultiClassLoader}.
 *
 * @author Paolo Melissari
 */
@RunWith(Enclosed.class)
public class TestMultiClassLoader{
	private static ClassLoader SYSTEM_LOADER = MultiClassLoader.class.getClassLoader();
    private static ClassLoader THREAD_LOADER = MultiClassLoader.THREAD_LOADER;
    

/*
    public static class TestFindClass{
    	private static MultiClassLoader _loader = new MultiClassLoader(SYSTEM_LOADER);
	    @Test
	    public void FindClassTest(){
	    	try {
				_loader.findClass(null);
				fail("findClass should fail with null argument");
			} catch (ClassNotFoundException e) {
			}
			
			try {
				Class result  = _loader.findClass(" ");
				fail("findClass should fail with empty argument");
			} catch (ClassNotFoundException e) {
				
			}
			
			try {	
				Class result = _loader.findClass("java.lang.Integer");
				assertNotNull(result);
				assertEquals(result,Integer.class);
			} catch (ClassNotFoundException e) {
				fail("findClass should not fail with java.lang.Integer argument");
			}
			
	    	
	    }
    }
    */
	@RunWith(Parameterized.class)
	public static class TestMultiClassLoaderaddClassLoaders {
	    private ClassLoader FOO_LOADER = new FooLoader();
	    private ClassLoader FOO_LOADER2 = new FooLoader2();
	    private static MultiClassLoader _loader = new MultiClassLoader(THREAD_LOADER,SYSTEM_LOADER);
	    private static MultiClassLoader _loader0 = new MultiClassLoader();
	    private static MultiClassLoader _loader1 = new MultiClassLoader();
	    private static MultiClassLoader _loader2 = new MultiClassLoader();
	    private static MultiClassLoader _loader3 = new MultiClassLoader();
	    private int index;
	    private MultiClassLoader multi;
	    private boolean expectedResult;
	    
	    @Before
	    public void setUp() {
	        _loader1.addClassLoader(THREAD_LOADER);
	        _loader1.addClassLoader(SYSTEM_LOADER);
	        _loader2.addClassLoader(FOO_LOADER);
	        _loader2.addClassLoader(FOO_LOADER2);
	        _loader3.addClassLoader(THREAD_LOADER);
	        _loader3.addClassLoader(FOO_LOADER);
	    }
	  
	    @Parameterized.Parameters
	    public static Collection<Object[]> ClassLoaderParameters() {
	    	return Arrays.asList(new Object[][] {
	    		//Test suite minimale
	    		{-1, null, false},
	    		{0, _loader0, false},
	    		{0, _loader1, false},
	    		{0, _loader2, true},
	    		{0, _loader3, true},
	    		});
	    };
	    
	    public TestMultiClassLoaderaddClassLoaders(int index, MultiClassLoader multi, boolean expectedResult) {
	    	this.index = index;
	    	this.multi = multi;
	    	this.expectedResult = expectedResult;
	    }
	
	    @Test
	    public void TestaddClassLoaders() {
	    	boolean actualResult = _loader.addClassLoaders(index,multi);
	    	assertEquals(actualResult, expectedResult);
	    	
	    }
	   
	}
	 private static final class FooLoader extends ClassLoader {
	    }
	    
	    private static final class FooLoader2 extends ClassLoader {
	
	    }
}
