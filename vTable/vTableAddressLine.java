package oop;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

public class vTableAddressLine{
	//String vTableClass;
	String methodname;
	boolean typecast;
	String classname;
	String vTableLine;
	//String parameters;
	boolean overloaded;
	HashSet<String> overrides;
	
	//for typecasting
	String returntype;
	String parameters;
	
	vTableClass parent;
	vTableLayoutLine matchinglayout;
	vTableMethodLayoutLine matchingmethod;
	
	public vTableAddressLine(vTableClass parentable){
		parent = parentable;
		overloaded = false;
		typecast = false;
		overrides = new HashSet<String>();
	}
	
	public void setMethodName(String methodnamable){
		methodname = methodnamable;
	}
	
	public void setTypeCast(String returntype, String parameters){
		typecast = true;
		this.returntype = returntype;
		if(parameters != null)
			this.parameters = parameters;
		else{
			this.parameters = " \b";
		}
		//currentaddress.setTypeCast("(" + currentmethod.returntype + "(*)(" 
	    //			+ this.classname + currentmethod.parameters + "))");
		
	}
	
	public void setOverride(String s){
		overrides.add(s);
	}
	
	public void setClassName(String classnamable){
		classname = classnamable;
	}
	
	public void setOverload(){
		overloaded = true;
		//parameters = parameterssource;
	}
	
	public void setMatching(vTableLayoutLine layout, vTableMethodLayoutLine method){
		matchinglayout = layout;
		matchingmethod = method;
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
	public void writeFile(BufferedWriter writer, vTableClass currentclass){
		if(methodname.equals("__isa")){
			vTableLine = methodname + "(";
			vTableLine = vTableLine + "__" + currentclass.classname + "::" + "__class()" + ")";
		}
		else if(methodname.equals("__delete")){
			vTableLine = ", \r" + methodname + "(";
			vTableLine = vTableLine + "&__" + currentclass.classname + "::" + "__delete" + ")";
		}
		else{
			if(overloaded == true){
				vTableLine = ", \r" + methodname + matchingmethod.parameters.replace(",", "_") + "(";
			}
			else{
				vTableLine = ", \r" + methodname + "(";
			}
			
			if(!this.classname.equals(currentclass.classname)){
				//if(!overrides.contains(currentclass.classname)){
					if(typecast == true){
						vTableLine = vTableLine + "(" + returntype + "(*)(" 
			    			+ currentclass.classname + parameters + "))"; 
					}
					else{
						vTableLine = vTableLine + "(" + matchingmethod.returntype + "(*)("
							+ currentclass.classname + matchingmethod.parameters + "))";
					}
				//}
				//else{
			//	}
				//currentaddress.setTypeCast("(" + currentmethod.returntype + "(*)(" 
			    //			+ this.classname + currentmethod.parameters + "))");
			}
			
			if(overloaded == true){
				if(!overrides.contains(currentclass.classname)){
					vTableLine = vTableLine + "&__" + classname + "::" + methodname //+ "_" 
					+ matchingmethod.parameters.replace(",", "_") + ")";
				}
				else{
					vTableLine = vTableLine + "&__" + currentclass.classname + "::" + methodname //+ "_" 
					+ matchingmethod.parameters.replace(",", "_") + ")";					
				}
					
			}
			else{
				if(!overrides.contains(currentclass.classname)){
					vTableLine = vTableLine + "&__" + classname + "::" + methodname + ")";
				}
				else{
					vTableLine = vTableLine + "&__" + currentclass.classname + "::" + methodname + ")";
				}
			}
			//vTableLine = vTableLine + "&__" + classname + "::" + methodname + ")";
		}
		
		try {
			//FileWriter writee = new FileWriter(file);
			//BufferedWriter writer = new BufferedWriter(writee);
			writer.write(vTableLine);
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printLine(){
		if(methodname.equals("__isa")){
			vTableLine = methodname + "(";
			vTableLine = vTableLine + "__" + classname + "::" + "__class()" + ")";
		}
		else{
			if(overloaded == true){
				vTableLine = ", \r" + methodname + "_" + matchingmethod.parameters.replace(",", "_") + "(";
			}
			else{
				vTableLine = ", \r" + methodname + "(";
			}
			
			if(!(typecast == true)){
				vTableLine = vTableLine + "(" + typecast + ")"; 
			}
			
			if(overloaded == true){
				vTableLine = vTableLine + "&__" + classname + "::" + methodname + "_" 
				+ matchingmethod.parameters.replace(",", "_") + ")";
			}
			else{
				vTableLine = vTableLine + "&__" + classname + "::" + methodname + ")";
			}
			//vTableLine = vTableLine + "&__" + classname + "::" + methodname + ")";
		}
		
		System.out.print(vTableLine);
	}
}
