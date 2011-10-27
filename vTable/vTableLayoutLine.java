package oop;

/*
 * VTableLayout line can be broken up into several parts
 * return type (*methodname) (parameters)
 * 
 * VTableAddress line can be broken up into several parts
 * methodname((anyspecialcasting)&class::methodname);
 * 
 *       
Class __isa;
int32_t (*hashCode)(Object);
bool (*equals)(Object, Object);
Class (*getClass)(Object);
String (*toString)(Object);
 */

public class vTableLayoutLine{
	String vTableClass;
	String returntype;
	String methodname;
	String parameters;
	String referenceparameter;
	String vTableLine;
	int parametercount = 0;
	
	public vTableLayoutLine(){
		parameters = ",";
	}
	
	public void setReturnType(String returnable){
		returntype = returnable;
	}
	
	public void setReferenceParameter(String reference){
		referenceparameter = reference;
	}
	
	public void setMethodName(String methodnamable){
		methodname = methodnamable;
	}
	
	public void resetParameters(String parameter){
		if(!parameter.equals(",")){
			parameters = parameter;
			parametercount = 1;
		}
	}
	
	public void setParameters(String parameter){
		if(parametercount == 0){
			parameters = parameters + parameter;
			parametercount++;
		}
		else{
			parameters = parameters + "," + parameter;
			parametercount++;
		}
	}
	
	public void setVTableClass(String typeclass){
		vTableClass = typeclass;
	}
	
	public void createStructLine(String definition){
		vTableLine = definition;
	}
	
	public void createVTableLine(){
		if(returntype == null)
			returntype = "void";
		vTableLine = returntype + " "; //+ "(*" + methodname + ") " + parameters + "); \r";
		
		if (methodname.equals("__isa")){
			vTableLine = vTableLine + methodname;
		}
		else{
			vTableLine = vTableLine + "(*" + methodname + ") ";
			vTableLine = vTableLine + "(" + referenceparameter;
			
			if (parametercount > 0){
				vTableLine = vTableLine + parameters;
			}
			vTableLine = vTableLine + ")";
		}
		
		vTableLine = vTableLine + "; \r";
	}
	
	public void printLine(){
		System.out.print(vTableLine);
	}
}