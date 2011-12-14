package oop.JavaGrinder.cc;

public class ccMainMethod extends ccMethod {
	
	public ccMainMethod(ccClass mClass){
		super("main", mClass);
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName);
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName, mIsStatic);
	}

	public String getName(){
		return super.getName();
	}
	
	public String publishDeclaration(){
		String decl = "int main()";
		return decl;
	}
	
	public String toString(){
		return "MAIN METHOD: " + super.toString();
	}
}
