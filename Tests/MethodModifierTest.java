package oop.Tests;

/**
 * Tests for multiple methods, accessors, static and final methods
 * @author user
 *
 */
public class MethodModifierTest {
	
	public void publicMethod(){
		
	}
	
	private void privateMethod(){
		
	}
	
	void undefinedMethod(){
		
	}
	
	protected void protectedMethod(){
		
	}
	
	static void staticMethod(){

	}
	
	
	final void finalMethod(){
		
	}
	
	public static void main(){
		MethodModifierTest.staticMethod();
		MethodModifierTest gentleman = new MethodModifierTest();
		gentleman.publicMethod();
		gentleman.privateMethod();
		gentleman.protectedMethod();
		gentleman.finalMethod();
	}

}
