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
	String returntype;
	String methodname;
	String parameters;
	String referencetype;
	String vTableLine;
	int parametercount = 0;
	
	vTableClass parent;
	
	public vTableLayoutLine(vTableClass parentable){
		parent = parentable;
		parameters = ",";
	}
	
	public void setReturnType(String returnable){
		returntype = returnable;
	}
	
	public void setReferenceType(String reference){
		referencetype = reference;
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
	
	/*
	//obsolete
	public void setVTableClass(String typeclass){
		vTableClass = typeclass;
	}
	
	//obsolete
	public void createStructLine(String definition){
		vTableLine = definition;
	}
	
	//obsolete
	public void createVTableLine(){
		if(returntype == null)
			returntype = "void";
		vTableLine = returntype + " "; //+ "(*" + methodname + ") " + parameters + "); \r";
		
		if (methodname.equals("__isa")){
			vTableLine = vTableLine + methodname;
		}
		else{
			vTableLine = vTableLine + "(*" + methodname + ") ";
			vTableLine = vTableLine + "(" + referencetype;
			
			if (parametercount > 0){
				vTableLine = vTableLine + parameters;
			}
			vTableLine = vTableLine + ")";
		}
		
		vTableLine = vTableLine + "; \r";
	}
	*/
	
	public void printLine(){
		if(returntype == null)
			returntype = "void";
		vTableLine = returntype + " "; //+ "(*" + methodname + ") " + parameters + "); \r";
		
		if (methodname.equals("__isa")){
			vTableLine = vTableLine + methodname;
		}
		else{
			vTableLine = vTableLine + "(*" + methodname + ") ";
			vTableLine = vTableLine + "(" + referencetype;
			
			if (parametercount > 0){
				vTableLine = vTableLine + parameters;
			}
			vTableLine = vTableLine + ")";
		}
		
		vTableLine = vTableLine + "; \r";
		
		System.out.print(vTableLine);
	}
}