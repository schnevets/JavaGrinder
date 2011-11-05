package oop;

import java.util.ArrayList;
import oop.ccMethod;

public class ccClass {
	private final String name;
	private final String access;
	private final boolean isStatic;
	private ArrayList<ccMethod> methods;
	
	public ccClass(String clName, String clAccess){
		name = clName;
		access = clAccess;
		isStatic = false;
		methods = new ArrayList<ccMethod>();
	}
	public ccClass(String clName, String clAccess, boolean clIsStatic){
		name = clName;
		access = clAccess;
		isStatic = clIsStatic;
		methods = new ArrayList<ccMethod>();
	}
	
	/** Adds method to class's list of methods */
	public void addMethod(ccMethod method){
		methods.add(method);
	}
	
	/** Gets the name of the class. Of course. */
	public String getName(){
		return name;
	}
	/** Gets the access level of the class (Public, Private, Protected) */
	public String getAccess(){
		return access;
	}
	/** Static? (y/n) */
	public boolean getIsStatic(){
		return isStatic;
	}
	/**
	 * Searches for and returns method named s.
	 * If the desired method is not in the list of methods, it returns a dummy method with the name "METHOD NOT FOUND".
	 * 
	 * @param s Name of desired method.
	 * @return The method with name s.
	 */
	public ccMethod getMethod(String s){
		for(int i = 0; i < methods.size(); i++){
			if(methods.get(i).getName() == s)
				return methods.get(i);
		}
		return new ccMethod("METHOD NOT FOUND");
	}
	/**
	 * If you know the index of the method you want, somehow, then this is a marginally faster way to get it.
	 * 
	 * @param i Index of desired method.
	 * @return The method at index i.
	 */
	public ccMethod getMethodAtIndex(int i){
		return methods.get(i);
	}
	
	public String toString(){
		String s = access + " class Name:\"" + name + "\" Static:" + isStatic + " Methods:";
		if(methods.size() > 0){
			for(int i = 0; i < methods.size(); i++){
				s += "\r\t" + methods.get(i);
			}
		}
		else
			s += "none";
		
		return s;
	}
	
}
