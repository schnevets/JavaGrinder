
package oop;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.tree.Visitor;

public class hMaster {
	LinkedList<vTableClass> classes;
	HashSet<String> classlist;
	
	LinkedList<String> waitqueue;
	
	//used by cc translator
	HashSet<String> namechanges;
	
	public hMaster(HashSet dependencies){
		classes = new LinkedList<vTableClass>();
		waitqueue = new LinkedList<String>();
		classlist = new HashSet<String>();
		hardIncludeJavaLangObject();
		
		Iterator iterate = dependencies.iterator();
		
		while(iterate.hasNext()){
			String currentfilename = (String)iterate.next();
			//File test = new File(currentfilename);
			translate(currentfilename);
		}
		
		//do not use for now
		/*
		while(!(waitqueue.isEmpty())){
			translate(waitqueue.pop());
		}
		*/
	}
	
	public void hardIncludeJavaLangObject(){
		vTableClass javaobject = new vTableClass("Object");
		javaobject.setNoWrite();
		hardIncludeJavaLangMethod(javaobject);
		hardIncludeJavaLangTable(javaobject);
		hardIncludeJavaLangAddress(javaobject);
		
		//classlist name subject to change
		classes.add(javaobject); //classes.add(javaclass); classes.add(javastring);
		classlist.add("Object"); //classlist.add("Class"); classlist.add("String");
	}
	
	public void hardIncludeJavaLangTable(vTableClass javaobject){
		javaobject.newTableLayout();
		javaobject.appendTableLayout("ReturnType", "Class");
		javaobject.appendTableLayout("MethodName", "__isa");
		javaobject.addTableLayout();
		javaobject.newTableLayout();
		javaobject.appendTableLayout("ReferenceType", "Object");
		javaobject.appendTableLayout("ReturnType", "int32_t");
		javaobject.appendTableLayout("MethodName", "hashCode");
		javaobject.addTableLayout();
		javaobject.newTableLayout();
		javaobject.appendTableLayout("ReferenceType", "Object");
		javaobject.appendTableLayout("ReturnType", "bool");
		javaobject.appendTableLayout("MethodName", "equals");
		javaobject.appendTableLayout("Parameters", "Object");
		javaobject.addTableLayout();
		javaobject.newTableLayout();
		javaobject.appendTableLayout("ReferenceType", "Object");
		javaobject.appendTableLayout("ReturnType", "Class");
		javaobject.appendTableLayout("MethodName", "getClass");
		javaobject.addTableLayout();
		javaobject.newTableLayout();
		javaobject.appendTableLayout("ReferenceType", "Object");
		javaobject.appendTableLayout("ReturnType", "String");
		javaobject.appendTableLayout("MethodName", "toString");
		javaobject.addTableLayout();
	}
	
	public void hardIncludeJavaLangAddress(vTableClass javaobject){
		javaobject.newTableAddress();
		javaobject.appendAddress("ClassName", "Object");
		javaobject.appendAddress("MethodName", "__isa");
		javaobject.addTableAddress();
		javaobject.newTableAddress();
		javaobject.appendAddress("ClassName", "Object");
		javaobject.appendAddress("MethodName", "hashCode");
		javaobject.addTableAddress();
		javaobject.newTableAddress();
		javaobject.appendAddress("ClassName", "Object");
		javaobject.appendAddress("MethodName", "equals");
		javaobject.addTableAddress();
		javaobject.newTableAddress();
		javaobject.appendAddress("ClassName", "Object");
		javaobject.appendAddress("MethodName", "getClass");
		javaobject.addTableAddress();
		javaobject.newTableAddress();
		javaobject.appendAddress("ClassName", "Object");
		javaobject.appendAddress("MethodName", "toString");
		javaobject.addTableAddress();
	}
	
	public void hardIncludeJavaLangMethod(vTableClass javaobject){
		javaobject.newMethodLayout();
		javaobject.appendMethod("Modifier", "static");
		javaobject.appendMethod("MethodName", "hashCode");
		javaobject.appendMethod("ReturnType", "int32_t");
		javaobject.appendMethod("ReferenceType", "Object");
		javaobject.addMethod();
		javaobject.newMethodLayout();
		javaobject.appendMethod("Modifier", "static");
		javaobject.appendMethod("MethodName", "equals");
		javaobject.appendMethod("ReturnType", "bool");
		javaobject.appendMethod("ReferenceType", "Object");
		javaobject.appendMethod("Parameters", "Object");
		javaobject.addMethod();
		javaobject.newMethodLayout();
		javaobject.appendMethod("Modifier", "static");
		javaobject.appendMethod("MethodName", "getClass");
		javaobject.appendMethod("ReturnType", "Class");
		javaobject.appendMethod("ReferenceType", "Object");
		javaobject.addMethod();
		javaobject.newMethodLayout();
		javaobject.appendMethod("Modifier", "static");
		javaobject.appendMethod("MethodName", "toString");
		javaobject.appendMethod("ReturnType", "String");
		javaobject.appendMethod("ReferenceType", "Object");
		javaobject.addMethod();
		
		/*
		vTableClass javaclass = new vTableClass("Class", "Object");
		vTableClass javastring = new vTableClass("String", "Object");
		*/
	}
	
	//temporary testing platform for hMaster
	public static void main(String[] args){
		HashSet<String> set = new HashSet<String>();
		set.add(args[0]); //testing one file for now so we can get away with this
		
		hMaster tester = new hMaster(set);
	}
	
	//question, how to sort out which classes belong to which file
	//likely solution would be to keep a list of all classes belonging to the current file
	public void translate(String sourcefile){
		//String[] args = {"-printJavaAST",sourcefile};
		//String[] args = {"-returnJavaAST", sourcefile};
		Node sourceAST = new ASTGenerator().generateAST(sourcefile);
		//System.out.println(sourceAST.isEmpty());
		final String currentsource = sourcefile;
		new Visitor() {
			//class variables
			vTableClass currentclass;
			vTableLayoutLine currentlayout;
			vTableAddressLine currentaddress;
			vTableMethodLayoutLine currentmethod;
			vTableForwardDeclarations forwarddeclarations;
			LinkedList<String> namespace;
			boolean missingsuper;
			String dataLayout;
			String operation;
			
			/**
			 * Possible Parents: None
			 * Writes to Elements: No
			 * 
			 * @param n GNode from the parser
			 */
			public void visitCompilationUnit(GNode n){
				//System.out.println("made it");
				namespace = new LinkedList<String>();
				for(Object o : n){
					if (o instanceof Node){ 
						missingsuper = false;
						dispatch((Node)o);
					}
				}
				//visit(n);
				//currentclass.printLines();
			}    		

			/**
			 * Possible Parents: 
			 * Writes to Elements: No
			 * 
			 * covers byte, int, short, long
			 * @param n
			 */
			public void visitIntegerLiteral(GNode n){
				String returnable = n.getString(0);
//				if(operation.equals("dataLayout")){
//					dataLayout = dataLayout + returnable;
//				}
				visit(n);
			}

			/**
			 * Possible Parents: 
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitBooleanLiteral(GNode n){
				String returnable = n.getString(0);
				visit(n);
			}

			/**
			 * Possible Parents:
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitFloatingPointLiteral(GNode n){
				String returnable = n.getString(0);
				visit(n);
			}

			/**
			 * Possible Parents:
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitStringLiteral(GNode n){
				String returnable = n.getString(0);
				visit(n);
			}
			
			/**
			 * 
			 * @param n
			 */
			public void visitFieldDeclaration(GNode n){
				if(operation.equals("dataLayout")){
					dataLayout = "";
				}
				visit(n);
			}

			/**
			 * Possible Parents: FieldDeclaration, MethodDeclaration
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitVoidType(GNode n){
				visit(n);
			}
			
			/**
			 * Possible Parents: FieldDeclaration
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitType(GNode n){
				visit(n);
			}

			/**
			 * Possible Parents: Type
			 * Writes to Elements: No
			 * 
			 * primitive type specific, ex int, double, float, etc.
			 * @param n
			 */
			public void visitPrimitiveType(GNode n){
				String returntype = n.getString(0);
				//naming checks
				if(returntype.equals("boolean")){
					returntype = "bool";
				}
				else if(returntype.equals("int")){
					returntype = "int32_t";
				}
				
				if(operation.equals("dataLayout")){
					dataLayout = dataLayout + returntype + " ";
				}
			}

			/**
			 * Possible Parents: Expression
			 * Writes to Elements: No
			 * 
			 * the name of the variable
			 * @param n
			 */
			public void visitPrimaryIdentifier(GNode n){
				visit(n);
			}

			/**
			 * Possible Parents: PackageDeclaration, Type
			 * Writes to Elements: No
			 * 
			 * 
			 * @param n
			 */
			public void visitQualifiedIdentifier(GNode n){
				String returnable = n.getString(0);
				
				if(operation.equals("Extension")){
					addSuperClass(returnable);
				}
				else if(operation.equals("dataLayout")){
					dataLayout = dataLayout + returnable + " ";
				}
			}

			/**
			 * Possible Parents: ClassDeclaration, FieldDeclaration, ConstructorDeclaration, MethodDeclaration
			 * Writes to Elements: No
			 * 
			 * 
			 * @param n
			 */
			public void visitModifiers(GNode n){
				visit(n);
			}

			/**
			 * Possible Parents: Modifiers
			 * Writes to Elements: No
			 * 
			 * public, private, protected, abstract, static, etc.
			 * @param n
			 */
			public void visitModifier(GNode n){
				String returnable = n.getString(0);
				if(operation.equals("ClassDeclaration")){
					currentclass.setModifier(returnable);
				}
				else if(operation.equals("dataLayout")){
					dataLayout = dataLayout + returnable;
				}
				visit(n);
			}

			/**
			 * Possible Parents: FieldDeclaration
			 * Writes to Elements: No
			 * 
			 * 
			 * @param n
			 */
			public void visitDeclarators(GNode n){
				visit(n);
			}

			/**
			 * Possible Parents: Declarators
			 * Writes to Elements: methodCC, dataLayoutH
			 * 
			 * Large construct for declaring a variable
			 * @param n
			 */
			public void visitDeclarator(GNode n){
				String addable = n.getString(0); 
				if(operation.equals("dataLayout")){
					dataLayout = dataLayout + addable;
//					if(!(n.getNode(2) == null)){
//						dataLayout = dataLayout + "=";
//					}
				}
				visit(n);
				if(operation.equals("dataLayout")){
					dataLayout = dataLayout + ";\r";
					currentclass.addDataLayout(dataLayout);
					//System.out.print("added datalayout " + dataLayout);
				}
			}

			/**
			 * Possible Parents: ClassDeclaration
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitClassBody(GNode n){
				operation = "dataLayout";
				System.out.println("Message: started on classbody " + currentclass.classname);
				visit(n);
			}

			/**
			 * Possible Parents: ClassBody
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitConstructorDeclaration(GNode n){
				operation = "Constructor";
				visit(n);
				operation = "dataLayout";
			}
			
			/**
			 * Possible Parents: ConstructorDeclaration, MethodDeclaration
			 * Writes to Elements: No
			 * 
			 * method arguments
			 * @param n
			 */
			public void visitFormalParameters(GNode n){
					int i = 0;
					for(Object o: n){
						if (o instanceof Node){
							dispatch((Node)o);
						}
						i++;
					}
			}

			/**
			 * Possible Parents: FormalParameters
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitFormalParameter(GNode n){
				for(Object o: n){
					if (o instanceof Node){
						dispatch((Node)o);
					}
				}			
			}
			
			/**
			* Possible Parents: ExpressionStatement, EqualityExpression, AdditiveExpression, Expression
			* Writes to Elements: No
			*
			* @param n
			*/
			public void visitCallExpression(GNode n){
				for(Object o: n){
					if (o instanceof Node){
						Node c = (Node)o;
						if (c.hasName("SelectionExpression")){
							dispatch((Node)o);
						}
					}
				}			
				visit(n);
			}
			
			public void visitExtension(GNode n){
				operation = "Extension";
				visit(n);
			}
			
			/**
			 * Retreive the namespaces for the class
			 * @param n
			 */
			public void visitPackageDeclaration(GNode n){
				//System.out.println("package seen");
				Node qualifier = n.getNode(1);
				for(Object o : qualifier){
					//System.out.println((String)o);
					namespace.add((String)o);
				}
			}

			/**
			 * Possible Parents: CompilationUnit
			 * Writes to Elements: forwardDeclarationsH, dataLayoutH
			 * 
			 * @param n
			 */
			public void visitClassDeclaration(GNode n){
				if(classlist.contains(n.getString(1))){
					System.out.println("Message: already did this class");
					return;
				}
				currentclass = new vTableClass(n.getString(1));
				System.out.println("Message: created new class" + currentclass.classname);
				Iterator<String> iterate = namespace.iterator();
				while(iterate.hasNext()){
					String spacable = iterate.next();
					currentclass.addNameSpace(spacable);
					System.out.println("Message: found namespace " + spacable);
				}
				
				//missingsuper = false;  //set in compilationunit instead
				
				//if there is no explicit extension node, then inherits Object by default
				if(n.getNode(3) == null){
					addSuperClass("Object");
					System.out.println("Message: added Object inheritance");
					System.out.println("Message: missingsuper test " + missingsuper);
				}
				//System.out.println("classname is " + n.getString(1));
				
				operation = "ClassDeclaration";
				visit(n);
				if(missingsuper == true){
					System.out.println("Message: missing superclass");
					return;
				}
				else{
					//eventually add currentclass to the classlist and to the classes linked list
				}
				//System.out.println("missingsuper test = " + missingsuper);
				
				currentclass.printLines();
			}

			public void visitArguments(GNode n){
				visit(n);
			}
			
			public void visitMethodDeclaration(GNode n){
				operation = "Method";
				visit(n);
			}
			
			public void addSuperClass(String s){
				if(classlist.contains(s)){
					Iterator iterate = classes.iterator();
					while(iterate.hasNext()){
						vTableClass classy = (vTableClass) iterate.next();
						if(classy.classname.equals(s)){
							currentclass.addSuperClass(classy);
							break;
						}
					}
				}
				//add to the queue
				else{
					waitqueue.add(currentsource);
					missingsuper = true;
				}
			}
			
			/**
			 * 
			 * @param n
			 */
			public void visit(Node n) {
				//System.out.println("test");
				for (Object o : n)
					if(missingsuper == true){
						return;
					}
					else if (o instanceof Node){ 
						dispatch((Node)o);
					}
			}
		}.dispatch(sourceAST);

	}
}	