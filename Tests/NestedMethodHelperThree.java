package oop.Tests;

public class NestedMethodHelperThree {

	public NestedMethodHelperThree getSelf(){
		return this;
	}
	
	public NestedMethodHelperTwo getOneGiveTwo(NestedMethodHelperOne n){
		return new NestedMethodHelperTwo();
	}
	
	public NestedMethodHelperThree getTwoGiveThree(NestedMethodHelperTwo n){
		return new NestedMethodHelperThree();
	}
	
	public NestedMethodHelperOne getThreeGiveOne(NestedMethodHelperThree n){
		return new NestedMethodHelperOne();
	}
	
	
}