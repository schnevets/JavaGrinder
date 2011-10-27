package oop;

public class vTableMethodLayoutLine {
	String modifier;
	String returntype;
	String methodname;
	String parameters;
	String referencetype;
	int parametercount;
	String vTableLine;
	
	public vTableMethodLayoutLine(){
		parametercount = 0;
	}
	
	public void setModifer(String modifiable){
		modifier = modifiable;
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
	
	public void createVTableLine(){
		vTableLine = modifier + " " + returntype + " " + methodname + "(" + referencetype;
		if (parametercount > 0){
			vTableLine = vTableLine + parameters;
		}
		vTableLine = vTableLine + "); \r";
	}
	
	public void printLine(){
		System.out.print(vTableLine);
	}
}
