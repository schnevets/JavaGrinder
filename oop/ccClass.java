package oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import oop.ccBlock;



public class ccClass {
	private final String name;
	private final String access;
	private final boolean isStatic;
	private ArrayList<ccConstructor> constructors;
	private ArrayList<ccMethod> methods;
	private int mCount;
	private HashMap<String, ccVariable> fields;
	private ArrayList<String> packageNames;
	public boolean constructorOverWritten;
	private ArrayList<String> instanceVariables;
	private ccClass superClass;
	
	public ccClass(String clName, String clAccess){
		name = clName;
		access = clAccess;
		isStatic = false;
		packageNames = new ArrayList<String>();
	}
	public ccClass(String clName, String clAccess, boolean clIsStatic){
		name = clName;
		access = clAccess;
		isStatic = clIsStatic;
		methods = new ArrayList<ccMethod>();
		constructors = new ArrayList<ccConstructor>();
		ccConstructor emptyCon = new ccConstructor(this.getName(), "public", new String[0], new String[0], this);
		ccBlock emptyBlock = new ccBlock();
		emptyBlock.blockLines.add("{\n");
		emptyBlock.blockLines.add("}\n");
		emptyCon.setBlock(emptyBlock);
		constructors.add(emptyCon);
		constructorOverWritten = false;
		fields = new HashMap<String, ccVariable>();
		packageNames = new ArrayList<String>();
		instanceVariables=new ArrayList<String>();
		superClass = null;
		mCount = 0;
	}
	
	public void addSuper(ccClass sClubSeven){
		superClass = sClubSeven;
	}
	/** Adds method to class's list of methods */
	public void addMethod(ccMethod method){
		methods.add(method);
		mCount++;
	}
	/** Adds constructor to class's list of constructors */
	public void addConstructor(ccConstructor constructor){
		if(constructor.hasNoArgs()){
			constructors.remove(0);
			constructorOverWritten = true;
		}
		constructors.add(constructor);
	}
	
	/** Adds field to class's hashmap of fields */
	public void addField(String n, String t, boolean s){
		fields.put(n, new ccVariable(n, t, this, s));
	}
	/** Sets the name of the class' package */
	public void addPackage(String[] currentPackage){
		for(String s : currentPackage){
		packageNames.add(s);
		}
	}
	
	/** Gets field hashmap */
	public HashMap getFields(){
		return fields;
	}
	
	/** Gets the name of the class. Of course. */
	public String getName(){
		return name;
	}
	/** Gets the name of the class with '__' before it. Yep. A whole class just for that. */
	public String get_Name(){
		return "__" + name;
	}
	/** Gets the access level of the class (Public, Private, Protected) */
	public String getAccess(){
		return access;
	}
	/** Static? (y/n) */
	public boolean getIsStatic(){
		return isStatic;
	}
	/** Gets the name of the class' package */
	public ArrayList<String> getPackage(){
		return packageNames;
	}
	/**
	 * Searches for and returns method named s with parameters param[].
	 * If the desired method is not in the list of methods, it returns a dummy method with the name "METHOD NOT FOUND".
	 * 
	 * @param s Name of desired method.
	 * @return The method with name s.
	 */
	public ccMethod getMethod(String s, String[] param, LinkedList<ccClass> classList){
		ccMethod ret = new ccMethod("METHOD NOT FOUND", this);
		for(int i = 0; i < methods.size(); i++){
			if(methods.get(i).match(s, param, classList)){
				ccMethod tempM = methods.get(i);
				if((param.length>0) && tempM.getParamTypes()[1] == param[0]){
					return tempM;
				}
				else{
					if(ret.getName().equals("METHOD NOT FOUND"))
						ret = tempM;
					else if((param.length != 0) && isBetter(tempM.getParamTypes()[tempM.getParamTypes().length - 1],
							ret.getParamTypes()[ret.getParamTypes().length -1], param[(param.length-1)])){
						ret = tempM;
					}
				}
			}	
		}
		return ret;
	}
	
	public boolean hasMethod(String s, String[] param){
		for(int i = 0; i < methods.size(); i++){
			if(methods.get(i).match(s, param))
				return true;
		}
		return false;
	}
	public String getMethodName(String s, String[] param, LinkedList<ccClass> classList){
		for(int i = 0; i < methods.size(); i++){
			if(methods.get(i).match(s, param, classList))
				return methods.get(i).getName();
		}
		return "This is an error.";
	}
	
	public boolean isBetter(String newThing, String oldThing, String target){
		if(target.startsWith("int")){
			if(oldThing.equals("int8_t")){
			}
			else if(oldThing.equals("int16_t")){
				if(newThing.equals("int8_t"))		return true;
			}
			else if(oldThing.equals("int32_t")){
				if(newThing.equals("int8_t"))		return true;
				if(newThing.equals("int16_t"))		return true;
			}
			else if(oldThing.equals("int64_t")){
				return true;
			}
			return false;
		}
		return true;
	}
	
	public ArrayList<ccConstructor> getConstructors(){
		return constructors;
	}
	public int getMethodCount(){
		return mCount;
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
	 * 
	 * @param i Index of desired constructor.
	 * @return The constructor at index i.
	 */
	public ccConstructor getConstructorAtIndex(int i){
		return constructors.get(i);
	}
	
	public ccClass getSuperClass(){
		return superClass;
	}
	
	public void addInheritedMethods(){
		
		if(!(null == superClass) && !superClass.getName().equals("Object")){
			methods = superClass.bequeath(methods);
			fields = superClass.bequeath(fields);
		}
		
		// Object methods:
		String[] argumentType = new String[0];
		String[] argumentName = new String[0];
		if(!hasMethod("hashCode", argumentType))
			methods.add(new ccMethod("hashCode", this, "public", "int32_t", argumentType, argumentName));
		argumentType = new String[1];
		argumentType[0] = "Object";
		argumentName = new String[1];
		argumentName[0] = "other";
		if(!hasMethod("equals", argumentType)) 
			methods.add(new ccMethod("equals", this, "public", "bool", argumentType, argumentName));
		argumentType = new String[0];
		argumentName = new String[0];
		if(!hasMethod("getClass", argumentType))
			methods.add(new ccMethod("getClass", this, "public", "Class", argumentType, argumentName));
		if(!hasMethod("toString", argumentType))
			methods.add(new ccMethod("toString", this, "public", "String", argumentType, argumentName));
	}
	
	// meth is short for method
	public ArrayList<ccMethod> bequeath(ArrayList<ccMethod> meth){
		for(int i = 0; i<methods.size(); i++){
			boolean mFlag = true;
			for(int j = 0; j<meth.size(); j++){
				if(meth.get(j).match(methods.get(i))){
					mFlag = false;
				}
			}
			if(mFlag){
				meth.add(methods.get(i));
			}
		}
		return meth;
	}
	
	public HashMap<String, ccVariable> bequeath(HashMap<String, ccVariable> fi){
		LinkedList<ccVariable> iter = new LinkedList<ccVariable>(fields.values());
		for(int i=0; i<iter.size(); i++){
			if (!fi.containsKey(iter.get(i).getName()))
				fi.put(iter.get(i).getName(), iter.get(i));
		}
		return fi;
	}
	
	public ccVariable findField(String n) {
		return fields.get(n);
	}
	
	public void addInstanceVariable(String string) {
		instanceVariables.add(string);
	}
	public ArrayList<String> getInstanceVariables() {
		return instanceVariables;
	}
	
}
