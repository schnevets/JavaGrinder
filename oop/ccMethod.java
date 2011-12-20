package oop;

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
		name = mName;
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
	
	public String getReturnType(){
		return returnType;
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
	
	/**
	 * Sees if this method matches the given name and parameter types.
	 * This version properly checks for implicit upcasts, and so this is the one that should be used
	 * everywhere other than in ccClass.
	 */
	public boolean match(String mName, String[] mparameterType, LinkedList<ccClass> classList){
		if(!mName.contentEquals(originalName))								return false;
		int offset = 1;
		if(isStatic){
			offset = 0;
		}
		if((mparameterType.length + offset) != parameterType.length)		return false;
		boolean check = true;
		for(int i=0; i< mparameterType.length; i++){
			if(!mparameterType[i].contentEquals(parameterType[i+1]) && !parameterType[i+offset].contentEquals("Object")){
				check = false;
				ccClass superClass = classList.get(3);
				for(int j=3; j<classList.size(); j++){
					superClass = classList.get(j);
					if(superClass.getName().equals(mName)){
						check = true;
						break;
					}
				}
				if(check){
					superClass = superClass.getSuperClass();
					check = false;
					while(!check){
						if(superClass.getName().contentEquals(parameterType[i+offset])){
							check = true;
						}
						else if(superClass.getName().contentEquals("Object")){
							break;
						}
						else{
							superClass = superClass.getSuperClass();
						}
					}
				}
			}
		}
		// If we still haven't found a method, and one of the parameters is an integer type (short, int, long) 
		// I have to check AGAIN to see if there's an appropriate method that uses one of the other two dingleberries.
		if(!check){
			for(int i=0; i< mparameterType.length; i++){
				if(mparameterType[i].matches("int8_t|int16_t|int32_t") && parameterType[i+1].matches("int64_t"))	check = true;
				if(mparameterType[i].matches("int8_t|int16_t") && parameterType[i+1].matches("int32_t|int64_t"))	check = true;
				if(mparameterType[i].matches("int8_t") && parameterType[i+1].matches("int16_t|int32_t|int64_t"))	check = true;
			}
		}
		return check;
	}
	
	/**
	 * Sees if this method matches the given name and parameter types.
	 * For use within ccClass, elsewhere use the version with three arguments.
	 */
	public boolean match(String mName, String[] mparameterType){
		if(!mName.contentEquals(originalName))								return false;
		if(isStatic){
			if(!Arrays.equals(parameterType, mparameterType))				return false;
		}
		else{
			if((mparameterType.length + 1) != parameterType.length)			return false;
			for(int i=0; i< mparameterType.length; i++){
				if(!mparameterType[i].contentEquals(parameterType[i+1]))	return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Sees if this method matches the given name and parameter types.
	 * For use within ccClass, elsewhere use the version with three arguments.
	 */
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
