package oop;

public class ccMethod {
	private final String name;
	private final String access;
	private final String returnType;
	private final String[] argumentType;
	private final String[] argumentName;
	private final boolean isStatic;
	
	// The first constructor only makes dummy methods, and will not actually be used
	public ccMethod(String mName){
		name = mName;
		access = "";
		returnType = "";
		argumentType = new String[0];
		argumentName = new String[0];
		isStatic = false;
	}
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mArgumentType, String[] mArgumentName){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		argumentType = mArgumentType;
		argumentName = mArgumentName;
		isStatic = false;
	}
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mArgumentType, String[] mArgumentName, boolean mIsStatic){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		argumentType = mArgumentType;
		argumentName = mArgumentName;
		isStatic = mIsStatic;
	}

	public String getName(){
		return name;
	}
	
	public String toString(){
		String s = "" + access + " method Name:\"" + name + "\" Return Type:\"" + returnType + "\" Arguments:(";
		for (int i = 0; i < argumentType.length; i++){
			if(i != 0) s += ", ";
			s += argumentType[i] + " " + argumentName[i];
		}
		s += ") Static:" + isStatic;
		return s;
	}
	
}
