package oop;

import java.util.LinkedList;

public class vTableClass {
	String classname;
	LinkedList<String> namespace;
	//String struct;
	vTableClass superclass;  //may even want to give a vTableClass reference as the superclass
	LinkedList<vTableMethodLayoutLine> vMethodLayout;
	LinkedList<vTableLayoutLine> vTableLayout;
	LinkedList<vTableAddressLine> vTableAddress;
	
	//create a new linked list of vClassConstructors
	
	vTableMethodLayoutLine currentmethod;
	vTableLayoutLine currentlayout;
	vTableAddressLine currentaddress;
	
	boolean writeable = true;
	
	public vTableClass(String classnamable, vTableClass parenting){
		classname = classnamable;
		if(parenting == null){
			superclass = null;
		}
		else{
			superclass = parenting;
		}
	
		//struct = "struct " + "__" + struct;
		vMethodLayout = new LinkedList<vTableMethodLayoutLine>();
		vTableLayout = new LinkedList<vTableLayoutLine>();
		vTableAddress = new LinkedList<vTableAddressLine>();
		namespace = new LinkedList<String>();
	}
	
	public void setNoWrite(){
		writeable = false;
	}
	
	//Create a new vTableMethodLayoutLine
	public void newMethodLayout(){
		currentmethod = new vTableMethodLayoutLine(this);
	}
	
	public void addMethod(){
		vMethodLayout.add(currentmethod);
	}
	
	public void appendMethod(String command, String arg){
		if(command.equals("ReferenceType")){
			currentmethod.setReferenceType(arg);
		}
		else if(command.equals("Modifier")){
			currentmethod.setModifer(arg);
		}
		else if(command.equals("ReturnType")){
			currentmethod.setReturnType(arg);
		}
		else if(command.equals("MethodName")){
			currentmethod.setMethodName(arg);
		}
		else if(command.equals("Parameters")){
			currentmethod.setParameters(arg);
		}
		else{
			System.out.println("Invalid command " + command);
		}
	}
	
	//Create a new vTableLayoutLine
	public void newTableLayout(){
		currentlayout = new vTableLayoutLine(this);
	}
	
	public void addTableLayout(){
		vTableLayout.add(currentlayout);
	}
	
	//note to self, declare the createline method in vtablemethodlayoutline obsolete and move the functionality
	//to the printline method instead
	public void appendTableLayout(String command, String arg){
		if(command.equals("ReferenceType")){
			currentlayout.setReferenceType(arg);
		}
		else if(command.equals("ReturnType")){
			currentlayout.setReturnType(arg);
		}
		else if(command.equals("MethodName")){
			currentlayout.setMethodName(arg);
		}
		else if(command.equals("Parameters")){
			currentlayout.setParameters(arg);
		}
		else{
			System.out.println("Invalid command " + command);
		}
	}
	
	//Create a new vTableAddressLine
	public void newTableAddress(){
		currentaddress = new vTableAddressLine(this);
	}
	
	public void addTableAddress(){
		vTableAddress.add(currentaddress);
	}
	
	//note to self, make the createline method in vtableaddressline obsolete, move the functionality
	//to the printline method instead
	public void appendAddress(String command, String arg){
		if(command.equals("TypeCast")){
			currentaddress.setTypeCast(arg);
		}
		else if(command.equals("ClassName")){
			currentaddress.setClassName(arg);
		}
		else if(command.equals("MethodName")){
			currentaddress.setMethodName(arg);
		}
		else{
			System.out.println("Invalid command " + command);
		}
	}
	
	public void printLines(){
		System.out.print("struct " + "__" + classname + "{ \r");
		System.out.print("__" + classname + "_VT*" + " __vptr;\r\r");
		System.out.print("__" + classname + "();\r\r");
		while(!vMethodLayout.isEmpty()){
			vTableMethodLayoutLine current = vMethodLayout.pop();
			current.printLine();
		}
		System.out.print("\r");
		System.out.print("static Class __class(); \r\r");
		System.out.print("static " + "__" + classname +"_VT _vtable;\r");
		System.out.print("};\r\r");
		
		System.out.print("struct __" + classname + "_VT { \r");
		while(!vTableLayout.isEmpty()){
			vTableLayoutLine current = vTableLayout.pop();
			current.printLine();
		}
		System.out.print("\r");
		System.out.print("__" + classname + "_VT()\r:  ");
		while(!vTableAddress.isEmpty()){
			vTableAddressLine current = vTableAddress.pop();
			current.printLine();
		}
		System.out.print("\r};\r");
	}
	
}

/*

				public void readJavaLang(){

					//vTableLayoutH
					vTableLayoutLine line = new vTableLayoutLine();
					line.setVTableClass("Object");
					line.setReturnType("Class");
					line.setMethodName("__isa");
					line.createVTableLine();
					OvTableLayoutH.add(line);
					line = new vTableLayoutLine();
					line.setVTableClass("Object");
					line.setReturnType("int32_t");
					line.setMethodName("hashCode");
					line.setReferenceParameter("Object");
					line.createVTableLine();
					OvTableLayoutH.add(line);
					line = new vTableLayoutLine();
					line.setVTableClass("Object");
					line.setReturnType("bool");
					line.setMethodName("equals");
					line.setReferenceParameter("Object");
					line.setParameters("Object");
					line.createVTableLine();
					OvTableLayoutH.add(line);
					line = new vTableLayoutLine();
					line.setVTableClass("Object");
					line.setReturnType("Class");
					line.setMethodName("getClass");
					line.setReferenceParameter("Object");
					line.createVTableLine();
					OvTableLayoutH.add(line);
					line = new vTableLayoutLine();
					line.setVTableClass("Object");
					line.setReturnType("String");
					line.setMethodName("toString");
					line.setReferenceParameter("Object");
					line.createVTableLine();
					OvTableLayoutH.add(line);
					
					//vTableAddressH
					vTableAddressLine liner = new vTableAddressLine();
					
					liner.setVTableClass("Object");
					liner.setClassname("__Object");
					liner.setMethodName("__isa");
					liner.createVTableLine();
					OvTableAddressH.add(liner);
					liner = new vTableAddressLine();
					liner.setVTableClass("Object");
					liner.setClassname("__Object");
					liner.setMethodName("hashCode");
					liner.createVTableLine();
					OvTableAddressH.add(liner);
					liner = new vTableAddressLine();
					liner.setVTableClass("Object");
					liner.setClassname("__Object");
					liner.setMethodName("equals");
					liner.createVTableLine();
					OvTableAddressH.add(liner);
					liner = new vTableAddressLine();
					liner.setVTableClass("Object");
					liner.setClassname("__Object");
					liner.setMethodName("getClass");
					liner.createVTableLine();
					OvTableAddressH.add(liner);
					liner = new vTableAddressLine();
					liner.setVTableClass("Object");
					liner.setClassname("__Object");
					liner.setMethodName("toString");
					liner.createVTableLine();
					OvTableAddressH.add(liner);
					
					//testing
					while(!(OvTableLayoutH.isEmpty())){
						vTableLayoutLine current = OvTableLayoutH.pop();
						System.out.print(current.vTableLine);
					}
					while(!(OvTableAddressH.isEmpty())){
						vTableAddressLine current = OvTableAddressH.pop();
						System.out.print(current.vTableLine);
					}
					
				}
				
public void convertVLayoutPrelim(String classname){
					//clone the OvTableLayoutH
					LinkedList<vTableLayoutLine> cloneOL = new LinkedList<vTableLayoutLine>();
					int counter = 0;
					try{
						while(true){
						vTableLayoutLine current = OvTableLayoutH.get(counter);
						vTableLayoutLine news = new vTableLayoutLine();
						news.setMethodName(current.methodname);
						news.resetParameters(current.parameters);
						news.setReferenceParameter(current.referenceparameter);
						news.setReturnType(current.returntype);
						news.setVTableClass(current.vTableClass);
						cloneOL.add(news);
						counter = counter + 1;
						}
					}
					catch(Exception e){
						//System.out.println("counter = " + counter);
					}
					
					
					LinkedList<vTableLayoutLine> newclone = new LinkedList<vTableLayoutLine>();
					while(!(cloneOL.isEmpty())){
						vTableLayoutLine current = cloneOL.pop();
						current.setReferenceParameter(classname);
						current.setVTableClass(classname);
						current.createVTableLine();
						newclone.add(current);
					}
					
					//testing
					
					while(!(newclone.isEmpty())){
						vTableLayoutLine current = newclone.pop();
						System.out.print(current.vTableLine);
					}
					
					//return newclone;
					vTableLayoutH.addAll(newclone);
				}
				
				//in progress conversion of addresslines to the right reference types
				public void convertVTableAddress(String classname){
					LinkedList<vTableAddressLine> cloneOA= new LinkedList<vTableAddressLine>();
					int counter = 0;
					try{
						while(true){
						vTableAddressLine current = OvTableAddressH.get(counter);
						vTableAddressLine news = new vTableAddressLine();
						//news.resetParameters(current.parameters);
						//news.setReferenceParameter(current.referenceparameter);
						//news.setReturnType(current.returntype);
						news.setVTableClass(current.vTableClass);
						news.setTypeCast(current.typecast);
						news.setClassname(current.classname);
						news.setMethodName(current.methodname);
						cloneOA.add(news);
						counter = counter + 1;
						}
					}
					catch(Exception e){
						//System.out.println("counter = " + counter);
					}
					
					LinkedList<vTableAddressLine> newclone = new LinkedList<vTableAddressLine>();
					while(!(cloneOA.isEmpty())){
						vTableAddressLine current = cloneOA.pop();
						//current.setReferenceParameter(classname);
						current.setVTableClass(classname);
						if(current.methodname.equals("getClass")){
							//current.classname = "__" + classname;
							current.typecast = "(Class(*)(__" + classname + "))";
						}
						current.createVTableLine();
						newclone.add(current);
					}
					
					vTableAddressH.addAll(newclone);
				}
				
				public void searchForMethodOverride(){
					String classsearch = currentLayoutLine.vTableClass;
					//System.out.println(classsearch);
					String methodsearch = currentLayoutLine.methodname;
					//System.out.println(methodsearch);
					String parametersearch = currentLayoutLine.parameters;
					//System.out.println(parametersearch);
					String returntypesearch = currentLayoutLine.returntype;
					//System.out.println(returntypesearch);
					//System.out.println();
					int counter = 0;
					try{
						boolean found = false;
						while(true){
							vTableLayoutLine current = vTableLayoutH.get(counter);
							//if (current.vTableClass.equals(classsearch) && current.returntype.equals(returntypesearch) && current.methodname.equals(methodsearch) && current.parameters.equals(parametersearch)){
							if(current.vTableLine.equals(currentLayoutLine.vTableLine))	{
								vTableLayoutH.remove(counter);
								found = true;
								//System.out.println("found over");
								break;
							}
							counter = counter + 1;
						}
						if(found == true){
							counter = 0;
							classsearch = currentAddressLine.vTableClass;
							methodsearch = currentAddressLine.methodname;
							//System.out.println("looking for " + methodsearch);
							while(true){
								vTableAddressLine current = vTableAddressH.get(counter);
								//System.out.println(current.vTableClass + " " + current.methodname);
								if(current.vTableClass.equals(classsearch) && current.methodname.equals(methodsearch) && !current.methodname.equals(null)){
									vTableAddressH.remove(counter);
									break;
								}
								counter = counter + 1;
							}
						}
					}
					catch (Exception e){
						
					}
				}
 */
