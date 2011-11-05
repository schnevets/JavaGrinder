package oop;

public class ccConstructor {
	private final String name;
	private final String access;
	private final String[] parameterType;
	private final String[] parameterName;
	
	// The first constructor only makes dummy... constructors... and will not actually be used
	public ccConstructor(String mName){
			name = mName;
			access = "";
			parameterType = new String[0];
			parameterName = new String[0];
	}
	public ccConstructor(String conName, String conAccess, String[] pType, String[] pName){
		name = conName;
		access = conAccess;
		parameterType = pType;
		parameterName = pName;
	}
	
	public String getName(){
		return name;
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
