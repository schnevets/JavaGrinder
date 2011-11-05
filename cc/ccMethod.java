package oop;

public class ccMethod {
	private final String name;
	private final String access;
	private final String returnType;
	private final String[] argumentType;
	private final boolean isStatic;
	
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mArgumentType){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		argumentType = mArgumentType;
		isStatic = false;
	}
	public ccMethod(String mName, String mAccess, String mReturnType, String[] mArgumentType, boolean mIsStatic){
		name = mName;
		access = mAccess;
		returnType = mReturnType;
		argumentType = mArgumentType;
		isStatic = mIsStatic;
	}

	public String getName(){
		return name;
	}
	
	
	
}
