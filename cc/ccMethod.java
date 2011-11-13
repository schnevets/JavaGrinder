package oop;

public class ccMethod {
	private final String name;
	private final String access;
	private final String returnType;
	private final String[] parameterType;
	private final String[] parameterName;
	private final boolean isStatic;
	
	// The first constructor only makes dummy methods, and will not actually be used
	public ccMethod(String mName){
		name = mName;
		access = "";
		returnType = "";
		parameterType = new String[0];
		parameterName = new String[0];
		isStatic = false;
	}
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		parameterType = mparameterType;
		parameterName = mparameterName;
		isStatic = false;
	}
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		parameterType = mparameterType;
		parameterName = mparameterName;
		isStatic = mIsStatic;
	}

	public String getName(){
		return name;
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
