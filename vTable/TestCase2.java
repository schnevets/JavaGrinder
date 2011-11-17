package oop.crapstackers;
public class TestCase2{
	int zeroed = 0 + (1 * 3);
	public double wobbles = 0;
	String stringer = " ";
	boolean crazy;
	
	public TestCase2(){
		zeroed = 0;
	}

	public TestCase2(int examples, String capable){
		zeroed = 1;
	}

	public void createStuff(int wack, double stuff){
		wobbles = wack * stuff;
	}

	public String getZeroed(){
		zeroed = 0;
		return "zeroed out";
	}

	public String toString(){
		return "new toString yay";
		
	}
}

