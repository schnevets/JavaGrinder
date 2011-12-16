package oop.JavaGrinder.Tests;

public class NestedMethodHelperOne {

	public NestedMethodHelperOne getSelf(){
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
