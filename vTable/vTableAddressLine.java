package oop;

public class vTableAddressLine{
	String vTableClass;
	String methodname;
	String typecast;
	String classname;
	String vTableLine;
	
	public vTableAddressLine(){
		
	}
	
	public void setMethodName(String methodnamable){
		methodname = methodnamable;
	}
	
	public void setTypeCast(String typecastable){
		typecast = typecastable;
	}
	
	public void setClassname(String classnamable){
		classname = classnamable;
	}
	
	public void setVTableClass(String classable){
		vTableClass = classable;
	}
	
	public void createVTableLine(){
		//methodname((anyspecialcasting)&class::methodname);

		if(methodname.equals("__isa")){
			vTableLine = methodname + "(";
			vTableLine = vTableLine + "&" + "__" + classname + "::" + "__class()" + ")";
		}
		else{
			vTableLine = ", \r" + methodname + "(";
			if(!(typecast == null)){
				vTableLine = vTableLine + "(" + typecast + ")"; 
			}
			vTableLine = vTableLine + "&" + classname + "::" + methodname + ")";
		}
	}
	
	public void printLine(){
		System.out.print(vTableLine);
	}
	
	public void createStructLine(String definition){
		vTableLine = definition;
	}
}
