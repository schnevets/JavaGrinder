package oop;

import java.util.LinkedList;

public class vTableForwardDeclarations {
	LinkedList<String> forwarddeclarations;
	LinkedList<String> typedeclarations;
	
	public vTableForwardDeclarations(){
		forwarddeclarations = new LinkedList<String>();
		typedeclarations = new LinkedList<String>();
	}
	
	public void addForwardDeclaration(String type){
		String addition = "struct " + "__" + type + ";";
		forwarddeclarations.add(addition);
	}
	
	public void addForwardVTable(String type){
		String addition = "struct " + "__" + type + "_VT" + ";";
		forwarddeclarations.add(addition);
	}
	
	public void addTypeDeclarations(String type){
		String addition = "typedef " + "__" + type + "* " + type + ";";
		typedeclarations.add(addition);
	}
	
	public void printLines(){
		while(!forwarddeclarations.isEmpty()){
			System.out.print(forwarddeclarations.pop() + "\r");
		}
		System.out.print("\r \r");
		while(!typedeclarations.isEmpty()){
			System.out.print(typedeclarations.pop() + "\r");
		}
		System.out.println("\r\r");
	}
}
