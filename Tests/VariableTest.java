package oop.Tests;

/** 
 * Tests instance, method, static, final, assignment, parameter variables, ints
 */
public class VariableTest {
	
	int instanceVariableOne;
	
	int instanceVariableTwo = 5;
	
	static int staticVariable = 0;
	
	public VariableTest(int i){
		instanceVariableOne = i;
	}

	public int getInstanceVariable(){
		return instanceVariableOne;
	}
	
	public int getStaticVariable(){
		return staticVariable;
	}
	
	public int setVariable(int i){
		int q = i;
		return q;
	}
	
	public void setFinalVariable(int i){
		final int q = i;
	}
	
}
