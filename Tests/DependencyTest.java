package oop.JavaGrinder.Tests;

/**
 * Tests for assignments and calls to other classes
 */
public class DependencyTest {

	CompileTest otherTest;
	
	public DependencyTest(CompileTest c){
		otherTest = c;		
	}
	
	public void setOtherTest(){
		otherTest = new ExtensionTest();
	}
	
	public void callOtherMethod(){
		SystemOutTest t = new SystemOutTest();
		t.printThis("Dependencies work!");
	}
	
}
