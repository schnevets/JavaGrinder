package oop;

import java.util.Iterator;
import java.util.LinkedList;

public class vTableData {
	LinkedList<String> modifiers;
	String name;
	String type;
	
	public vTableData(){
		modifiers = new LinkedList<String>();
	}
	
	public void setName(String s){
		name = s;
	}
	
	public void setType(String s){
		type = s;
	}
	
	public void addModifier(String s){
		modifiers.add(s);
	}
	
	public String getDataString(){
		String assembled = " ";
		Iterator<String> iterate = modifiers.iterator();
		while (iterate.hasNext()){
			assembled = assembled + iterate.next() + " ";
		}
		assembled = assembled + type + " " + name + ";\r";
		return assembled;
	}
}
