package oop.crapstacker;
import oop.crapstackers.*;
public class TestCase1 {
	public static void main (String args []){
		float floater = 6.02e23f;
		short shorts = 1;
		double doubler = 2.5;
		long longer = 12345L;
		String stringer = "This is the test string";
		int stacker = 1;
		int grand = stacker + 10;
		//grand = stacker - 10;
		grand = stacker * 10;
		TestCase2 crossfiletest = new TestCase2();
		//grand = stacker / 10 * 10;
		//grand = stacker % 10;
		//Tester testing = new Tester(grand);
		//testing.createStuff(grand, doubler);
		crossfiletest.createStuff(grand, doubler);
		if(true){
			for(;;){
				break;
			}
			if(true){
			}
			else{
		        }
		}
		while(true){
			int snacks = 1;
			if(true){
			}
			if(false)
			   snacks = 2;
			break;
		}
		int test = 1;
		do{
			test = test + 1;
		}
		while(test<3);
		System.out.println(grand);
		System.out.println(crossfiletest.wobbles);
		System.out.println(stringer);
		System.out.println("Finished all the things!");
	}

	protected void test(){
	}
}

class Tester{
	int zeroed = 0;
	double wobbles = 0;
	String stringer = " ";

	public Tester (int examples){
		zeroed = 1;
	}

	public void createStuff(int wack, double stuff){
		wobbles = wack * stuff;
	}

	public String getZeroed(){
		zeroed = 0;
		return "zeroed out";
	}
}
