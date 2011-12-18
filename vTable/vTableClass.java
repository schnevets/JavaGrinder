package oop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class vTableClass {
	String classname;
	String modifier;
	LinkedList<String> namespace;
	HashSet<String> includes;
	HashSet<String> usings;
	HashSet<String> overloadedmethods;
	vTableClass superclass;  
	LinkedList<vTableMethodLayoutLine> vMethodLayout;
	LinkedList<vTableLayoutLine> vTableLayout;
	LinkedList<vTableAddressLine> vTableAddress;
	LinkedList<vClassConstructor> vClassConstructors;
	vTableForwardDeclarations forwarddeclarations;
	LinkedList<String> dataLayout;
	File file;
	
	//create a new linked list of vClassConstructors
	
	vTableMethodLayoutLine currentmethod;
	vTableLayoutLine currentlayout;
	vTableAddressLine currentaddress;
	vClassConstructor currentconstructor;
	
	boolean writeable;
	
	public vTableClass(String classnamable){
		writeable = true;
		classname = classnamable;
		superclass = null;
	
		//struct = "struct " + "__" + struct;
		vMethodLayout = new LinkedList<vTableMethodLayoutLine>();
		vTableLayout = new LinkedList<vTableLayoutLine>();
		vTableAddress = new LinkedList<vTableAddressLine>();
		vClassConstructors = new LinkedList<vClassConstructor>();
		forwarddeclarations = new vTableForwardDeclarations();
		namespace = new LinkedList<String>();
		dataLayout = new LinkedList<String>();
		overloadedmethods = new HashSet<String>();
		includes = new HashSet<String>();
		usings = new HashSet<String>();
		
	}
	
	public void setFile(File writeto){
		file = writeto;
		//System.out.println("file set to " + file.getName());
	}
	
	public void setNoWrite(){
		writeable = false;
	}
	
	public void setModifier(String mod){
		modifier = mod;
	}
	
	public void addUsings(String state){
		System.out.println("added using " + state);
		usings.add(state);
	}
	
	public void addSuperClass(vTableClass parenting){
		//System.out.println("adding superclass " + parenting.classname);
		superclass = parenting;
		copysupertable();
	}
	
	public void addForwardDeclaration(String currentclass){
		forwarddeclarations.addForwardDeclaration(currentclass);
		forwarddeclarations.addForwardVTable(currentclass);
		forwarddeclarations.addTypeDeclarations(currentclass);
	}
	
	public void addIncludes(String s){
		if(!includes.contains(s)){
			//System.out.println("added includes " + s);
			includes.add(s);
		}
	}
	
	public boolean checkJavaLang(String s){
		//System.out.println("checking " + s);
		if(s.equals("String") || s.equals("Object") || s.equals("Class") || s.equals("Exception")){
			return true;
		}
		return false;
	}
	
	public void addAdditionalInclude(String s){
		if(!includes.contains("\"" + s + ".h\"") && (checkJavaLang(s) != true)){
			includes.add("\"" + s + ".h\"");
			addUsings(s);
		}
	}
	
	public void copysupertable(){
		Iterator<vTableMethodLayoutLine> methoditerate = superclass.vMethodLayout.iterator();
		Iterator<vTableAddressLine> addressiterate = superclass.vTableAddress.iterator();
		Iterator<vTableLayoutLine> layoutiterate = superclass.vTableLayout.iterator();
		Iterator<String> superincludes = superclass.includes.iterator();
		
		while(superincludes.hasNext()){
			includes.add(superincludes.next());
		}
		
		if(superclass.classname.equals("Object")){
			includes.add("\"java_lang.h" + "\"");
		}
		else{
			includes.add("\"" + superclass.classname + ".h" + "\"");
		}
		
		while(methoditerate.hasNext()){
			currentlayout = layoutiterate.next();
			currentlayout.setReferenceType(classname);
			currentaddress = addressiterate.next();
			if(currentaddress.methodname.equals("__isa") || currentaddress.methodname.equals("__delete")){
				currentaddress.setClassName(classname);
				//System.out.println("copying specialmethod " + currentaddress.methodname);
				if(currentaddress.methodname.equals("__delete")){
					currentmethod = methoditerate.next();
					//currentlayout.setReferenceType(this.classname);
					currentmethod.setReferenceType("__" + classname + "*");
					currentlayout.setReferenceType("__" + classname + "*");
					vMethodLayout.add(currentmethod);
				}
				vTableLayout.add(currentlayout);
				vTableAddress.add(currentaddress);
			}
			else{
				currentmethod = methoditerate.next();
				if((currentmethod.visibility.equals("public") || currentmethod.visibility.equals("protected")) && currentmethod.staticcheck != true){
					//System.out.println("copying supermethod " + superclass.classname + currentmethod.methodname);
					currentmethod.setReferenceType(classname);
					currentaddress.setTypeCast(currentmethod.returntype,currentmethod.parameters);
					vTableLayout.add(currentlayout);
					vTableAddress.add(currentaddress);
					vMethodLayout.add(currentmethod);
				}
			}
		}
	}
	
	public void addNameSpace(String s){
		namespace.add(s);
	}
	
	public void addDataLayout(String s){
		dataLayout.add(s);
	}
	
	public void newConstructor(){
		currentconstructor = new vClassConstructor(this);
	}
	
	public void appendConstructor(String s){
		currentconstructor.addParameter(s);
	}

	public void addConstructor(){
		vClassConstructors.add(currentconstructor);
	}
	
	public void checkOverride(){
		int index = 0;
		boolean override = false;
		try{
			//note that eventually the while loop will run into an index out of bounds error
			//but the exception will be caught in the try catch loop, effectively breaking from the loop
			//whether an override or not an override
				while(true){
					vTableMethodLayoutLine scanline = vMethodLayout.get(index);
					if(scanline.methodname.equals(currentmethod.methodname) && scanline.parameters.equals(currentmethod.parameters)){
						if(scanline.finalcheck == true){
							throw new Exception(currentmethod.methodname + ":Invalid Override, Super Method is final");
						}
						else{
							vMethodLayout.remove(index);
							vTableLayout.remove(index + 1);
							vTableAddress.remove(index + 1);
							override = true;
							break;
						}
					}
					index++;
				}
		}
		catch(Exception e){
			if(e.getMessage().contains("Invalid Override")){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			//nothing doin, intentional exception for when an index out of bounds error occurs (meaning no override found)
		}
	}
	
	public void checkOverload(){
		int index = 0;
		boolean overload = false;
		try{
			//note that eventually the while loop will run into an index out of bounds error
			//but the exception will be caught in the try catch loop, effectively breaking from the loop
			//whether an override or not an override
				while(true){
					vTableMethodLayoutLine scanline = vMethodLayout.get(index);
					if(scanline.methodname.equals(currentmethod.methodname)){
						scanline.setOverload();
						if(!scanline.parent.equals(this.superclass)){
							superclass.addOverload(scanline.methodname);
						}
						superclass.addOverload(currentmethod.methodname);
						currentmethod.setOverload();
						currentlayout.setOverload();
						currentaddress.setOverload();
						overloadedmethods.add(currentmethod.methodname);
						overload = true;
						break;
					}
					index++;
				}
		}
		catch(Exception e){
			//nothing doin, intentional exception for when an index out of bounds error occurs (meaning no override found)
		}
	}
	
	public void addOverload(String name){
		overloadedmethods.add(name);
	}
	
	public void resolveOverloads(){
		//this method is called right before printing to resolve overloaded methods and the other objects
		//needed to be edited because of them
		Iterator iterate = vMethodLayout.iterator();
		while(iterate.hasNext()){
			vTableMethodLayoutLine current = (vTableMethodLayoutLine)iterate.next();
			if(overloadedmethods.contains(current.methodname)){
				current.setOverload();  //this setOverload method cascades to the matching tablelayout and address lines
			}
		}
//		iterate = vTableLayout.iterator();
//		while(iterate.hasNext()){
//			vTableLayoutLine current = (vTableLayoutLine)iterate.next();
//			if(current.matchingmethod.overloaded == true || overloadedmethods.contains(current.methodname)){
//				current.setOverload();
//			}
//		}
	}
	
	//Create a new vTableMethodLayoutLine
	public void newMethodLayout(){
		currentmethod = new vTableMethodLayoutLine(this);
	}
	
	//note that by the time this method is called, all of the related method
	//entries will have been assembled as well so they are finalized in terms of visitors
	//includes currentaddress, currentmethod, and currentlayout
	public void addMethod(){
		setMatching();
		checkOverride();  
		checkOverload();
		vMethodLayout.add(currentmethod);
	}
	
	public boolean checkStaticPrivate(){
		//System.out.println("checking static private of " + currentmethod.methodname);
		//System.out.println(currentmethod.methodname + " is " + currentmethod.modifier);
		if(currentmethod.staticcheck == true || currentmethod.visibility.contains("private")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void setMatching(){
		if(currentlayout != null && currentmethod != null && currentaddress != null){
			currentmethod.setMatching(currentlayout, currentaddress);
			currentaddress.setMatching(currentlayout, currentmethod);
			currentlayout.setMatching(currentaddress, currentmethod);
		}
	}
	
	public void appendMethod(String command, String arg){
		if(command.equals("ReferenceType")){
			currentmethod.setReferenceType(arg);
		}
		else if(command.equals("Modifier")){
			//System.out.println("method for " + currentmethod.methodname + " set to " + arg);
			currentmethod.setModifer(arg);
			//currentmethod.setVisiblity(arg);
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
		else if(command.equals("ObjectVisiblity")){
			currentmethod.setVisiblity(arg);
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
		//checkOverride("TableLayout");
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
		//checkOverride("Address");
		vTableAddress.add(currentaddress);
	}
	
	//note to self, make the createline method in vtableaddressline obsolete, move the functionality
	//to the printline method instead
	public void appendAddress(String command, String arg){
		if(command.equals("TypeCast")){
			//currentaddress.setTypeCast(arg);
		}
		else if(command.equals("ClassName")){
			currentaddress.setClassName(arg);
		}
		else if(command.equals("MethodName")){
			currentaddress.setMethodName(arg);
		}
		else{
			System.out.println("Invalid commawriternd " + command);
		}
	}
	
	public void writeFile(BufferedWriter writer, HashMap usingStates){
		try {
			//FileWriter writee = new FileWriter(file);
			//BufferedWriter writer = new BufferedWriter(writee);
			Iterator<String> iterate = includes.iterator();
			writer.write("#pragma once \r\r");
			while(iterate.hasNext()){
				writer.write("#include " + iterate.next() + "\r");
			}
			writer.write("\r");
			iterate = namespace.iterator();
			while(iterate.hasNext()){
				writer.write("namespace " + iterate.next() + "{\r");
			}
			
			forwarddeclarations.writefile(writer);
			//using statements
			writer.write("using namespace java::lang;\r");  //always by default
			//other statements
			iterate = usings.iterator();
			HashSet<String> writtenUsings = new HashSet<String>();
			while(iterate.hasNext()){
				String s = (String)usingStates.get(iterate.next());
				if(!writtenUsings.contains(s)){
					writer.write( s + ";\r");
					writtenUsings.add(s);
				}
			}
			
			writer.write("\r");
			
			writer.write("struct " + "__" + classname + "{ \r");
			writer.write("__" + classname + "_VT*" + " __vptr;\r");
			
			iterate = dataLayout.iterator();
			while(iterate.hasNext()){
				writer.write(iterate.next());
			}
			
			writer.write("\r");
			//writer.flush();
			//writer.close();
			//the constructors
			//writer.write("__" + classname + "();\r\r");  the basic constructor, no arguments
			if(vClassConstructors.isEmpty()){
				vClassConstructor noargs = new vClassConstructor(this);
				vClassConstructors.add(noargs);
			}
			Iterator<vClassConstructor> constructorIterate = vClassConstructors.iterator();
			boolean noargcheck = false;
			while(constructorIterate.hasNext()){
				vClassConstructor constructor = constructorIterate.next();
				if(constructor.parameters == null){
					noargcheck = true;
				}
				constructor.writeFile(writer);
			}
			if(noargcheck == false){
				vClassConstructor noargs = new vClassConstructor(this);
				noargs.writeFile(writer);
			}
			
			Iterator<vTableMethodLayoutLine> methodIterate = vMethodLayout.iterator();
			while(methodIterate.hasNext()){
				vTableMethodLayoutLine current = methodIterate.next();
				current.writeFile(writer, this);
			}
			//writer = new BufferedWriter(writee);
			writer.write("\r");
			writer.write("static Class __class(); \r\r");
			writer.write("static " + "__" + classname +"_VT __vtable;\r");
			writer.write("};\r\r");
			
			writer.write("struct __" + classname + "_VT { \r");
			//writer.close();
			Iterator<vTableLayoutLine> tableIterate = vTableLayout.iterator();
			while(tableIterate.hasNext()){
				vTableLayoutLine current = tableIterate.next();
				current.writeFile(writer, this);
			}
			//writer = new BufferedWriter(writee);
			writer.write("\r");
			writer.write("__" + classname + "_VT()\r:  ");
			//writer.close();
			Iterator<vTableAddressLine> addressIterate = vTableAddress.iterator();
			while(addressIterate.hasNext()){
				vTableAddressLine current = addressIterate.next();
				current.writeFile(writer, this);
			}
			//writer = new BufferedWriter(writee);
			writer.write("{}\r};\r");
			//writer.close();
			iterate = namespace.iterator();
			while(iterate.hasNext()){
				iterate.next();
				writer.write("}\r");
			}
			//writer = new BufferedWriter(writee);
			writer.write("\r");
			writer.flush();
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//outdated console printer
	public void printLines(){
		Iterator<String> iterate = namespace.iterator();
		while(iterate.hasNext()){
			System.out.print("namespace " + iterate.next() + "{\r");
		}
		
		System.out.print("struct " + "__" + classname + "{ \r");
		System.out.print("__" + classname + "_VT*" + " __vptr;\r");
		
		while(!dataLayout.isEmpty()){
			System.out.print(dataLayout.pop());
		}
		
		System.out.print("\r");
		
		//the constructors
		//System.out.print("__" + classname + "();\r\r");  the basic constructor, no arguments
		while(!vClassConstructors.isEmpty()){
			vClassConstructor constructor = vClassConstructors.pop();
			constructor.printLine();
		}
		
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
		System.out.print("{}\r};\r");
		
		iterate = namespace.iterator();
		while(iterate.hasNext()){
			iterate.next();
			System.out.print("}\r");
		}
		System.out.print("\r");
	}
	
}