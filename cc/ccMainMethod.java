package oop;

public class ccMainMethod extends ccMethod {
	
	private String[] parameterType;
	private String[] parameterName;
	
	public ccMainMethod(ccClass mClass){
		super("main", mClass);
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName);
		parameterName = mparameterName;
		parameterType = mparameterType;
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName, mIsStatic);
		parameterName = mparameterName;
		parameterType = mparameterType;
	}

	public String getName(){
		return super.getName();
	}
	
	public String publishDeclaration(){
		String decl = "int main";
		decl += "(";
		for (int i = 0; i < parameterType.length; i++){
			if(i != 0) {decl += ", ";}
			decl += parameterType[i] + " " + parameterName[i];	
		}
		decl += ")";
		return decl;
	}
	
	public String toString(){
		return "MAIN METHOD: " + super.toString();
	}
}
