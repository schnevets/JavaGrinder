package oop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class vTableForwardDeclarations {
	LinkedList<String> forwarddeclarations;
	LinkedList<String> typedeclarations;
	HashSet<String> stored;
	
	public vTableForwardDeclarations(){
		forwarddeclarations = new LinkedList<String>();
		typedeclarations = new LinkedList<String>();
		stored = new HashSet<String>();
	}
	
	public boolean hasDeclarations(String s){
		if(stored.contains(s)){
			return true;
		}
		return false;
	}
	
	public void addForwardDeclaration(String type){
		String addition = "struct " + "__" + type + ";";
		forwarddeclarations.add(addition);
		//System.out.println(addition);
	}
	
	public void addForwardVTable(String type){
		String addition = "struct " + "__" + type + "_VT" + ";";
		forwarddeclarations.add(addition);
		//System.out.println(addition);
	}
	
	public void addTypeDeclarations(String type){
		String addition = "typedef " + " __rt::Ptr<__" + type + "> " + type + ";";
		typedeclarations.add(addition);
		stored.add(type);
		//System.out.println(addition);
	}
	
	public void writefile(BufferedWriter writer){
		//FileWriter writee;
		try {
			//writee = new FileWriter (file);
			//BufferedWriter writer = new BufferedWriter(writee);
			while(!forwarddeclarations.isEmpty()){
				writer.write(forwarddeclarations.pop() + "\r");
			}
			writer.write("\r \r");
			while(!typedeclarations.isEmpty()){
				writer.write(typedeclarations.pop() + "\r");
			}
			writer.write("\r\r");
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
