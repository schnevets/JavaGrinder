package oop;

import xtc.tree.GNode;

public class ccMethod {
	private String name;
	private final ccClass parentClass;
	private final String access;
	private final String returnType;
	private final String[] parameterType;
	private final String[] parameterName;
	private final boolean isStatic;
	private ccBlock block;
	
	// The first constructor only makes dummy methods, and will not actually be used
	public ccMethod(String mName, ccClass mClass){
		name = mName;
		parentClass = mClass;
		access = "";
		returnType = "";
		parameterType = new String[0];
		parameterName = new String[0];
		isStatic = false;
		block = new ccBlock();
	}
	public ccMethod(String mName, ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, ccBlock blk){
		name = mName;
		parentClass = mClass;
		access = mAccess;
		returnType = mReturnType;
		parameterType = mparameterType;
		parameterName = mparameterName;
		isStatic = false;
		block = blk;
	}
	public ccMethod(String mName, ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic, ccBlock blk){
		name = mName;
		parentClass = mClass;
		access = mAccess;
		returnType = mReturnType;
		parameterType = mparameterType;
		parameterName = mparameterName;
		isStatic = mIsStatic;
		block = blk;
	}

	public void mangleName(){
		name = parentClass + "__" + name;
		for (int i = 0; i < parameterType.length; i++){
			name += "__" + parameterType[i];
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String cppDeclaration(){
		String decl = "";
		if(isStatic){
			return decl;
		}
		else{
			decl = ccHelper.convertType(returnType) + " " + parentClass + "::" + name  + "(";
			for (int i = 0; i < parameterType.length; i++){
				if(i != 0) decl += ", ";
				decl += parameterType[i] + " " + parameterName[i];
			}
			return decl;
		}
	}
	public String toString(){
		String s = "" + access + " method Name:\"" + name + "\" Return Type:\"" + returnType + "\" Parameters:(";
		for (int i = 0; i < parameterType.length; i++){
			if(i != 0) s += ", ";
			s += parameterType[i] + " " + parameterName[i];
		}
		s += ") Static:" + isStatic;
		return s;
	}
}
