package oop.JavaGrinder.Tests;

/**
 * Tests methods throwing exceptions, try...catch statements, printStackTrace()
 */
public class ExceptionTest {
	
	public void throwMethod() throws Exception{
		
	}

	public void tryCatchMethod(Exception f){
		try{
			throw f;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
