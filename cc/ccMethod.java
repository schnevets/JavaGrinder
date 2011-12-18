package oop.JavaGrinder.cc;

import java.util.Arrays;
import java.util.LinkedList;

import xtc.tree.GNode;

public class ccMethod {
	private String name;
	private final String originalName;
	private boolean nameMangled;
	private final ccClass parentClass;
	public final String access;
	private final String returnType;
	private final String[] parameterType;
	private final String[] parameterName;
	public final boolean isStatic;
	private ccBlock block;
	
	// The first constructor only makes dummy methods, and will not actually be used
	public ccMethod(String mName, ccClass mClass){
		originalName = mName;
		parentClass = mClass;
		access = "";
		returnType = "";
		parameterType = new String[0];
		parameterName = new String[0];
		isStatic = false;
	}
	public ccMethod(String mName, ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName){
		name = mName;
		originalName = mName;
		nameMangled = false;
		parentClass = mClass;
		access = mAccess;
		returnType = mReturnType;
		parameterType = new String[mparameterType.length + 1];
		parameterType[0] = parentClass.getName();
		parameterName = new String[mparameterName.length + 1];
		parameterName[0] = "__this";
		for(int i=1; i<parameterType.length; i++){
			parameterType[i] = mparameterType[i - 1];
			parameterName[i] = mparameterName[i - 1];
		}
		isStatic = false;
	}
	public ccMethod(String mName, ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic){
		name = mName;
		originalName = mName;
		nameMangled = false;
		parentClass = mClass;
		access = mAccess;
		returnType = mReturnType;
		isStatic = mIsStatic;
		if(isStatic){
			parameterType = mparameterType;
			parameterName = mparameterName;
		}
		else {
			parameterType = new String[mparameterType.length + 1];
			parameterType[0] = parentClass.getName();
			parameterName = new String[mparameterName.length + 1];
			parameterName[0] = "__this";
			for(int i=1; i<parameterType.length; i++){
				parameterType[i] = mparameterType[i - 1];
				parameterName[i] = mparameterName[i - 1];
			}
		}
	}
	
	
	
	
	public void setBlock(ccBlock blk){
		block = blk;
	}
	
	public void changeThisToPointer(){
		parameterType[0] = "__" + parameterType[0] + "*";
	}
	
	// name_param1_param2_etc
	public void mangleName(){
		if(!nameMangled){
			for (int i = 1; i < parameterType.length; i++){
				name += "_" + parameterType[i];
			}
			nameMangled = true;
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String[] getParamTypes(){
		return parameterType;
	}
	public boolean match(String mName, String[] mparameterType){
		if(!mName.contentEquals(originalName))								return false;
//		System.out.println("~~~1 (" + originalName + ")");
		if(isStatic){
			if(!Arrays.equals(parameterType, mparameterType))				return false;
		}
		else{
			if((mparameterType.length + 1) != parameterType.length)			return false;
//			System.out.println("~~~2 (" + mparameterType.length + ")");
			for(int i=0; i< mparameterType.length; i++){
//				System.out.println("~~~3:" + i + " (" + mparameterType[i] + " - " + parameterType[i+1] + ")");
				System.out.println(mparameterType[i]);
				if(!mparameterType[i].contentEquals(parameterType[i+1]))	return false;
			}
		}
		
		return true;
	}
	
	public boolean match(ccMethod meth){
		if(!meth.getName().contentEquals(originalName))						return false;
		if(!Arrays.equals(parameterType, meth.getParamTypes()))				return false;
		
		return true;
	}
	
	public ccClass getParentClass(){
		return parentClass;
	}
	
	public String publishDeclaration(){
		String decl = ccHelper.convertType(returnType) + " " + 
				parentClass.get_Name() + "::" + name  + "(";
		for (int i = 0; i < parameterType.length; i++){
			if(i != 0) decl += ", ";
			if(!(isStatic&&(i==0))){
				decl += parameterType[i] + " " + parameterName[i];	
			}
		}
		decl += ")";
		return decl;
	}
	
	public LinkedList<String> publishBlock(){
		if(block!=null){
			return block.publish();
		}
		else{return null;}
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
