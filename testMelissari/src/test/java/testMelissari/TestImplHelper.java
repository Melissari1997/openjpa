package testMelissari;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;

import org.apache.openjpa.conf.OpenJPAConfiguration;
import org.apache.openjpa.conf.OpenJPAConfigurationImpl;
import org.apache.openjpa.enhance.PCRegistry;
import org.apache.openjpa.enhance.PersistenceCapable;
import org.apache.openjpa.kernel.OpenJPAStateManager;
import org.apache.openjpa.kernel.PCState;
import org.apache.openjpa.util.ImplHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import testMelissari.util.DummyPersistenceCapeable;
import testMelissari.util.ExtendedFoo;
import testMelissari.util.Foo;
import testMelissari.util.NotRegisteredDummyPersistenceCapeable;

@RunWith(Enclosed.class)
public class TestImplHelper {
	
	@RunWith(Parameterized.class)
	public static class TestIsManagedType{
		private OpenJPAConfiguration conf;
		private boolean expectedResult;
		private Class<?> inputClass;

		@Parameterized.Parameters
	    public static Collection<Object[]> ClassLoaderParameters() throws ClassNotFoundException {
	    	OpenJPAConfiguration conf = new OpenJPAConfigurationImpl(true);
	    	conf.setRuntimeUnenhancedClasses(0);
	    	OpenJPAConfiguration conf2 = new OpenJPAConfigurationImpl(false);
	    	DummyPersistenceCapeable registeredPC = new DummyPersistenceCapeable();
	    	NotRegisteredDummyPersistenceCapeable notRegisteredPC = new NotRegisteredDummyPersistenceCapeable();
	    	conf2.setRuntimeUnenhancedClasses(1);
	    	return Arrays.asList(new Object[][] {
	    		//Test suite minimale 
	    		{null, null, false},
	    		{conf, registeredPC.getClass(), true},
	    		{conf, Integer.class, false},
	    		
	    		//adeguacy

	    	   {conf2, Integer.class, false}, 
	     
	    	    //mutation
	    	    {conf2, null, false},
	    	    {conf, notRegisteredPC.getClass(), true},
	    	   // {null, Integer.class, false},
	
	    		
	    		});
	    		
	    };
	    public TestIsManagedType(OpenJPAConfiguration conf, Class<?> inputClass, boolean expectedResult) {
	    	this.conf = conf;
	    	this.inputClass = inputClass;
	    	this.expectedResult = expectedResult;
	    }
		@Test
		public void testisManagedType() {
			try{
				boolean result = ImplHelper.isManagedType(this.conf, this.inputClass);
				assertEquals(this.expectedResult, result);
			}catch(Exception e){
				if(this.conf != null && this.inputClass != null)
					fail("Should not raise a NullPointerException");
			}
			
		}	
	}

	@RunWith(Parameterized.class)
	public static class TestIsAssignable{
		private boolean expectedResult;
		private Class<?> inputClassTo;
		private Class<?> inputClassFrom;
		@Parameterized.Parameters
	    public static Collection<Object[]> ClassLoaderParameters() {
	    	return Arrays.asList(new Object[][] {
	    		//Test suite minimale
	    		{null, null,false},
	    		{new Foo().getClass(), new Foo().getClass(),true},
	    		{new ExtendedFoo().getClass(), new Foo().getClass(), true},
	    		{new Foo().getClass(), new Integer(1).getClass(),false},
	    		
	    		// adeguacy
	    		{new Foo().getClass(), null,false},
	    		{null, new Foo().getClass(),false},
	    		//mutation
	    		{new Foo().getClass(),String.class, false},
	    		
	    		
	    		});
	    };
	    public TestIsAssignable(Class<?> inputClassTo, Class<?> inputClassFrom,boolean expectedResult) {
	    	this.inputClassTo = inputClassTo;
	    	this.inputClassFrom = inputClassFrom;
	    	this.expectedResult = expectedResult;
	    }
		@Test
		public void testisAssignable() {
				boolean result = ImplHelper.isAssignable(this.inputClassFrom, this.inputClassTo);
				assertEquals(this.expectedResult, result);
		}
	}
	
	@RunWith(MockitoJUnitRunner.class)
	public static class TestGetUpdateFields{
		private BitSet expectedResult;
		private BitSet dirty;
		private BitSet flushed;
		
		@Before
		public void setUp() {
			BitSet expectedResult = new BitSet();
			expectedResult.set(0);
			this.expectedResult = expectedResult;
			
			BitSet dirty = new BitSet();
			dirty.set(0);
			dirty.set(1);
			this.dirty = dirty;
			
			BitSet flushed = new BitSet();
			flushed.set(1);
			this.flushed = flushed;
			
		}
		
		@Mock
		OpenJPAStateManager mockedStateManager = mock(OpenJPAStateManager.class);
		
		public void createMock(boolean isFlushed, boolean isFlushedDirty, BitSet dirty, BitSet flushed, PCState state) {
			when(mockedStateManager.isFlushed()).thenReturn(isFlushed);
			when(mockedStateManager.getPCState()).thenReturn(state);
			when(mockedStateManager.isFlushedDirty()).thenReturn(isFlushedDirty);
			when(mockedStateManager.getDirty()).thenReturn(dirty);
			when(mockedStateManager.getFlushed()).thenReturn(flushed);
		}
		
		@Test
		public void testgetUpdateFields() {
			BitSet result;
			try {
				createMock(false,false, null, null ,null);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertNull(result);
			}catch(Exception e) {
				
			}
			try {
				createMock(true,true,dirty,flushed, PCState.PDIRTY);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertEquals(result,expectedResult);
			}catch(Exception e) {
				
			}
			try {
				createMock(true,true, dirty, flushed ,PCState.PNEW);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertEquals(expectedResult,result);
			}catch(Exception e) {
				
			}
			
			PCState pc = new PCState();
			
			try {
				createMock(true,true,dirty,flushed, pc);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertNull(result);
			}catch(Exception e) {
				
			}
			
			//adeguacy
			try {
				createMock(true,false,dirty,flushed, PCState.PDIRTY);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertNull(result);
			}catch(Exception e) {
				
			}
			try {
				createMock(false,true,dirty,flushed, PCState.PDIRTY);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertEquals(dirty,result);
			}catch(Exception e) {
				
			}
			
			//mutation

			try {
				createMock(true,false, dirty, flushed ,PCState.PNEW);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertNull(result);
			}catch(Exception e) {
				
			}
			
			
			
			try {
				BitSet empty = new BitSet();
				createMock(false,true,empty,flushed, PCState.PDIRTY);
				result = ImplHelper.getUpdateFields(mockedStateManager);
				assertNull(result);
			}catch(Exception e) {
				
			}
			
		}
		
	}
	
}
	
	
