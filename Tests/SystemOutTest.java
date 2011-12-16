package oop.JavaGrinder.Tests;

/** 
 * Tests System.out.println(), Strings
 */
public class SystemOutTest {

	public SystemOutTest(){
		System.out.println("System.out.println() works!");
	}
	
	public void printThis(String s){
		System.out.println(s);
	}
	
	public static void main(){
		SystemOutTest gentleman = new SystemOutTest();
		gentleman.printThis("Cheerio!");
	}
	
}
