package oop.JavaGrinder.Tests;

/**
 * Tests if, if...else, booleans
 */
public class ConditionalTest {

	public void ifTester(){
		boolean maybe = true;
		if(maybe){
			maybe = false;
		}
	}
	
	public void ifElseTester(){
		boolean possibly = false;
		if(possibly){
			possibly = false;
		}
		else{
			possibly = true;
		}
	}
	
	public static void main(){
		ConditionalTest gentleman = new ConditionalTest();
	}
	
}
