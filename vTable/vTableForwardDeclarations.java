package oop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
		//System.out.println(addition);
	}
	
	public void addForwardVTable(String type){
		String addition = "struct " + "__" + type + "_VT" + ";";
		forwarddeclarations.add(addition);
		//System.out.println(addition);
	}
	
	public void addTypeDeclarations(String type){
		String addition = "typedef " + "__" + type + "* " + type + ";";
		typedeclarations.add(addition);
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
