package oop;

import java.util.HashSet;

public class Main {
	public static void main(String args[]){
		DependencyMaster dependsource = new DependencyMaster();
		//System.out.println(args[0]);
		HashSet<String> set = dependsource.getDependencies(args[0]);//new HashSet<String>();
		//set.add(args[0]); //testing one file for now so we can get away with this
		//Iterator iterate = set.iterator();
		//System.out.println(iterate.next());
		//System.out.println(iterate.next());
		hMaster tester = new hMaster(set);
		HashSet<String> overloadedmethods = tester.overloads;
		//placeholder for cc stuffs
	}
}
