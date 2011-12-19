package oop;

public class ccVariable {
	private boolean classVariable;
	private String name;
	private String type;
	private ccClass parent;
	private boolean isStatic;
	
	/**
	 * This constructor is for class variables created outside of a block.
	 */
	public ccVariable(String n, String t, ccClass p, boolean s){
		classVariable = true;
		name = n;
		type = t;
		parent = p;
		isStatic = s;
	}
	
	/**
	 * This constructor is for variables created inside a block.
	 */
	public ccVariable(String n, String t){
		classVariable = false;
		name = n;
		type = t;
	}
	
	public String publish(){
		if (classVariable && isStatic){
			return "__" + parent.getName() + "::" + name;
		}
		else if(classVariable){
			return "__this->" + name;
		}
		else{
			return name;
		}
	}
	
	public String getType(){
		return type;
	}
	public String getName(){
		return name;
	}
	
//	public String toString(){
//		return "name: " + name + ", type: " + type + ", parent: " +
//				parent.getName() + ", static?: " + isStatic;
//	}
	
}
