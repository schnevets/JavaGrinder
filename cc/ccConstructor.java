package oop.JavaGrinder.cc;

import java.util.LinkedList;

public class ccConstructor {
	private final String name;
	private final ccClass parentClass;
	private final String access;
	private final String[] parameterType;
	private final String[] parameterName;
	private ccBlock block;
	private boolean isNoArg;
	
	// The first constructor only makes dummy... constructors... and will not actually be used
	public ccConstructor(String mName, ccClass mClass){
		name = mName;
		parentClass = mClass;
		access = "";
		parameterType = new String[0];
		parameterName = new String[0];
		isNoArg = true;
	}
	public ccConstructor(String conName, String conAccess, String[] pType, String[] pName, ccClass mClass){
		name = conName;
		parentClass = mClass;
		access = conAccess;
		parameterType = pType;
		parameterName = pName;
		isNoArg = parameterName.length==0;
	}
	
	public void setBlock(ccBlock blk){
		block = blk;
	}
	public ccBlock getBlock(){
		return block;
	}
	public String getName(){
		return name;
	}
	public boolean hasNoArgs(){
		return isNoArg;
	}
	public String publishDeclaration(){
		String decl = "";
		decl = parentClass.get_Name() + "::__" + name  + "(";
		for (int i = 0; i < parameterType.length; i++){
			if(i != 0) decl += ", ";
			decl += parameterType[i] + " " + parameterName[i];
		}
		decl += ") : __vptr(&__vtable)";
		return decl;
	}

	public LinkedList<String> publishBlock(){
		return block.publish();
	}
	
	public String toString(){
		String s = "" + access + " method Name:\"" + name + "\" Parameters:(";
		for (int i = 0; i < parameterType.length; i++){
			if(i != 0) s += ", ";
			s += parameterType[i] + " " + parameterName[i];
		}
		s += ")";
		return s;
	}
	
}
