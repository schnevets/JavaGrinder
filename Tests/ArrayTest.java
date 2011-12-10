package oop.Tests;

/**
 * Tests arrays
 */
public class ArrayTest {
	
	String[] args;
	
	public ArrayTest(String[] array){
		args = array;
		args[1] = "Oodalalee";
		String s = args[0];
	}
	
	public int[] getIntArray(){
		return new int[20];
	}
	
	
}
