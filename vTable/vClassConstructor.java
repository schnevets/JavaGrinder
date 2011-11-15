package oop;

public class vClassConstructor {
	String parameters;
	
	vTableClass parent;
	
	public vClassConstructor(vTableClass parentable){
		parameters = null;
		parent = parentable;
	}
	
	public void addParameter(String param){
		if (parameters == null){
			parameters = param;
		}
		else{
			parameters = parameters + ", " + param;
		}
	}
	
	public void printLine(){
		System.out.print("__" + parent.classname + "(");
		if (parameters == null){
			System.out.print(");\r");
		}
		else{
			System.out.print(parameters + ");\r");
		}
	}
}
