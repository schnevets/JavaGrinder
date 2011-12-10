package oop.Tests;

/**
 *  Tests super, overriding methods, this
 */
public class OverrideTest extends ParameterReturnTest {

	public OverrideTest(int i){
		super(i);
	}
	
	public int getInt(){
		return 2;
	}
	
	public void callToSuper(){
		this.takeAndReturn(3);
	}
}
