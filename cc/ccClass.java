package oop;

import java.util.ArrayList;

public class ccClass {
	private final String name;
	private final String access;
	private final boolean isStatic;
	private ArrayList<ccConstructor> constructors;
	private ArrayList<ccMethod> methods;
	
	public ccClass(String clName, String clAccess){
		name = clName;
		access = clAccess;
		isStatic = false;
		methods = new ArrayList<ccMethod>();
		constructors = new ArrayList<ccConstructor>();
	}
	public ccClass(String clName, String clAccess, boolean clIsStatic){
		name = clName;
		access = clAccess;
		isStatic = clIsStatic;
		methods = new ArrayList<ccMethod>();
		constructors = new ArrayList<ccConstructor>();
	}
	
	/** Adds method to class's list of methods */
	public void addMethod(ccMethod method){
		methods.add(method);
	}
	/** Adds constructor to class's list of constructors */
	public void addConstructor(ccConstructor constructor){
		constructors.add(constructor);
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
		return new ccMethod("METHOD NOT FOUND", this);
	}
	/**
	 * Searches for and returns constructor named s.
	 * If the desired constructor is not found, it returns a dummy constructor with the name "CONSTRUCTOR NOT FOUND".
	 * This method was built under the assumption that we could be overloading constructors,
	 * I'm still not totally sure how that's gonna work.
	 * 
	 * @param s Name of desired constructor.
	 * @return The constructor with name s.
	 */
	public ccConstructor getConstructor(String s){
		for(int i = 0; i < constructors.size(); i++){
			if(constructors.get(i).getName() == s)
				return constructors.get(i);
		}
		return new ccConstructor("CONSTRUCTOR NOT FOUND", this);
	}
	public int getMethodCount(){
		return methods.size();
	}
	public int getConstructorCount(){
		return constructors.size();
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
	/**
	 * If you know the index of the constructor you want, somehow, then this is a marginally faster way to get it.
	 * This method was built under the assumption that we could be overloading constructors,
	 * I'm still not totally sure how that's gonna work.
	 * 
	 * @param i Index of desired constructor.
	 * @return The constructor at index i.
	 */
	public ccConstructor getConstructorAtIndex(int i){
		return constructors.get(i);
	}
	
	public String toString(){
		String s = access + " class Name:\"" + name + "\" Static:" + isStatic + " Constructors:";
		if(constructors.size() > 0){
			for(int i = 0; i < constructors.size(); i++){
				s += "\r\t" + constructors.get(i);
			}
			s += "\r";
		}
		else s += "none";
		s += " Methods:";
		if(methods.size() > 0){
			for(int i = 0; i < methods.size(); i++){
				s += "\r\t" + methods.get(i);
			}
		}
		else s += "none";

		return s;
	}
	
}
