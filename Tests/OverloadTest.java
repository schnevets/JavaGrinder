package oop.Tests;

/**
 * Tests overloaded methods, constructors
 */
public class OverloadTest {
	
	public OverloadTest(){
		
	}
	
	public OverloadTest(int i, String s){
		
	}
	
	public OverloadTest(CompileTest c){
		
	}
	
	public String overloadedMethod(String s){
		return s;
	}
	
	public void overloadedMethod(int i){
		
	}
	
	private int overloadedMethod(int i, String s){
		return i;
	}

	public static int overloadedMethod(){
		return 0;
		
	}
	
}
