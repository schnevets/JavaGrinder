package oop;

public class vTableMethodLayoutLine {
	String modifier;
	String returntype;
	String methodname;
	String parameters;
	String referencetype;
	int parametercount;
	String vTableLine;
	String visibility;
	
	vTableClass parent;
	
	public vTableMethodLayoutLine(vTableClass parentable){
		parent = parentable;
		parametercount = 0;
		modifier = "";
		returntype = "";
		methodname = "";
		parameters = "";
		referencetype = "";
		visibility = "";
	}
	
	public void setModifer(String modifiable){
		if(modifiable.equals("final")){
			modifier = "const";
		}
		else if(modifiable.equals("public") || modifiable.equals("private") || modifiable.equals("protected")){
			visibility = modifiable;
		}
		//modifier = modifiable;
	}
	
	public void setReturnType(String returntypable){
		returntype = returntypable;
	}
	
	public void setReferenceType(String reference){
		referencetype = reference;
	}
	
	public void setMethodName(String namable){
		methodname = namable;
	}
	
	public void setParameters(String parameter){
		if(parametercount == 0){
			parameters = "," + parameter;
			parametercount = 1;
		}
		else{
			parameters = parameters + "," + parameter;
			parametercount = parametercount + 1;
		}
		
	}
	
	//obsolete method
	/*
	public void createVTableLine(){
		vTableLine = modifier + " " + returntype + " " + methodname + "(" + referencetype;
		if (parametercount > 0){
			vTableLine = vTableLine + parameters;
		}
		vTableLine = vTableLine + "); \r";
	}
	*/
	
	public void printLine(){
		vTableLine = "static " + modifier + " " + returntype + " " + methodname + "(" + referencetype;
		if (parametercount > 0){
			vTableLine = vTableLine + parameters;
		}
		vTableLine = vTableLine + "); \r";
		
		System.out.print(vTableLine);
	}
}
