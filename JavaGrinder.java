package oop;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class JavaGrinder {
	public JavaGrinder(String args[]){
		DependencyMaster dependency = new DependencyMaster();
		dependency.checkDependencies(args);
		dependency.checkForFiles(args);
		HashSet<String> translateMe = new HashSet<String>();
		
		String dirName = args[0].substring(0, args[0].indexOf('.')) + "_Output";
		File directory = new File(dirName);
		directory.mkdir();
		
		for(int i=0; i< args.length; i++){
			translateMe.add(args[i]);
		}
		hMaster hTranslate = new hMaster(translateMe, directory);
		HashSet<String> overloads = hTranslate.overloads;
		ccMaster ccTranslate = new ccMaster(translateMe, overloads, directory);
		System.out.println("Checkit: done.");
	}
	
	public static void main(String args[]){
		JavaGrinder jg = new JavaGrinder(args);
	}
}
