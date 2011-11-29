package oop;

public class DependencyTester {

	public static void main(String[] args) {
		System.out.println((new DependencyMaster()).checkForFiles(args));
		System.out.println((new DependencyMaster()).checkDependencies(args));
	}

	
}
