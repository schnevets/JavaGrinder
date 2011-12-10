package oop;

import java.util.HashSet;

public class JavaGrinder {
	public JavaGrinder(String args[]){
		DependencyMaster dependency = new DependencyMaster();
		dependency.checkDependencies(args);
		dependency.checkForFiles(args);
		HashSet<String> translateMe = new HashSet<String>();
		for(int i=0; i< args.length; i++){
			translateMe.add(args[i]);
		}
		hMaster hTranslate = new hMaster(translateMe);
		HashSet<String> overloads = hTranslate.overloads;
		ccMaster ccTranslate = new ccMaster(translateMe, overloads);
		System.out.println("Checkit: done.");
	}
	
	public static void main(String args[]){
		JavaGrinder jg = new JavaGrinder(args);
	}
}
