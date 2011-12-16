package oop.JavaGrinder.Tests;

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
	
	public static void main(){
		VariableTest gentleman = new VariableTest(5);
		System.out.println(gentleman.getInstanceVariable());
		System.out.println(gentleman.getStaticVariable());
		System.out.println(gentleman.setVariable(2));
		gentleman.setFinalVariable(i);
	}
	
}
