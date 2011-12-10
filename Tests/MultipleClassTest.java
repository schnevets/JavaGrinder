package oop.Tests;

/**
 * Test multiple classes in the same file
 */
public class MultipleClassTest {
	public MultipleClassTest(){
		String s = ("Something shiny");
	}
	public void someMethod(){
	}
}

class TheOtherClass {
	public TheOtherClass(){
		String s = ("Something sinister");
	}
	public void someMethod(){
	}
	public String anotherMethod(){
		return "Yes";
	}
}