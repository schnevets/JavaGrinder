package oop.Tests;

/**
 * Tests nested classes
 */
public class NestedClassTest {
	
	public NestedClassTest(){	
	}

	public void someMethod(){
		int i;
	}
	
	public class innerClass {
		
		public innerClass(){	
		}
		
		public void someMethod(){
			int q;
		}
	}
	
	private class privateInnerClass {
		
		private privateInnerClass(){
		}
		
		public void anotherMethod(){
			int m;
		}
	}
	
}
