package oop;

public class ccMainMethod extends ccMethod {
	
	public ccMainMethod(ccClass mClass){
		super("main", mClass);
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, ccBlock blk){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName, blk);
	}
	public ccMainMethod(ccClass mClass, String mAccess, String mReturnType, String[] mparameterType, String[] mparameterName, boolean mIsStatic, ccBlock blk){
		super("main", mClass, mAccess, mReturnType, mparameterType, mparameterName, mIsStatic, blk);
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
