package oop;

public class vTableAddressLine{
	//String vTableClass;
	String methodname;
	String typecast;
	String classname;
	String vTableLine;
	
	vTableClass parent;
	
	public vTableAddressLine(vTableClass parentable){
		parent = parentable;
	}
	
	public void setMethodName(String methodnamable){
		methodname = methodnamable;
	}
	
	public void setTypeCast(String typecastable){
		typecast = typecastable;
	}
	
	public void setClassName(String classnamable){
		classname = classnamable;
	}
	
	/*
	//obsolete
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
	*/
	
	public void printLine(){
		if(methodname.equals("__isa")){
			vTableLine = methodname + "(";
			vTableLine = vTableLine + "__" + classname + "::" + "__class()" + ")";
		}
		else{
			vTableLine = ", \r" + methodname + "(";
			if(!(typecast == null)){
				vTableLine = vTableLine + "(" + typecast + ")"; 
			}
			vTableLine = vTableLine + "&__" + classname + "::" + methodname + ")";
		}
		
		System.out.print(vTableLine);
	}
}
