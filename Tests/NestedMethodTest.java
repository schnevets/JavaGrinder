package oop.JavaGrinder.Tests;

/**
 * Tests nested method calls
 */
public class NestedMethodTest {

	public NestedMethodTest(){
		NestedMethodHelperOne a = new NestedMethodHelperOne();
		NestedMethodHelperTwo b = new NestedMethodHelperTwo();
		NestedMethodHelperThree c = new NestedMethodHelperThree();
		
		a = a.getSelf();
		a.getTwoGiveThree(b);
		a.getTwoGiveThree(b.getSelf());
		a.getTwoGiveThree(b.getOneGiveTwo(a.getSelf()));
		a.getTwoGiveThree(b.getOneGiveTwo(c.getThreeGiveOne(c.getSelf())));
	}
	
}
