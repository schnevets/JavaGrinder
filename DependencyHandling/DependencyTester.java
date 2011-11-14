package oop;

public class DependencyTester {

	public static void main(String[] args) {
		System.out.println((new DependencyMaster()).getDependencies("/home/user/xtc/src/Translator/src/oop/Testfile.java").toString());
	}

	
}
