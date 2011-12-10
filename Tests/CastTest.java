package oop.Tests;

/**
 * Tests upcasts and downcasts
 */
public class CastTest {

	public boolean doesItCast(CompileTest c){
		return true;
	}

	public void downCastMethod(){
		Object o = new CompileTest();
		this.doesItCast((CompileTest)o);
	}
	
	public void upCastMethod(){
		ExtensionTest e = new ExtensionTest();
		this.doesItCast(e);
	}
	
}
