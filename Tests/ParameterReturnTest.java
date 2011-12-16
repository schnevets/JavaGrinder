package oop.JavaGrinder.Tests;

/**
 * Tests for method parameters and return types, ints
 */
public class ParameterReturnTest {
	
	public ParameterReturnTest(int i){
		i = 0;
	}
	
	public int getInt(){
		return 1;
	}
	
	public void takeInts(int i, int q){
		
	}
	
	public int takeAndReturn(int i){
		return i;
	}
	
	public static void main(){
		ParameterReturnTest gentleman = new ParameterReturnTest();
		gentleman.takeInts(1, 2);
	}
	
}
