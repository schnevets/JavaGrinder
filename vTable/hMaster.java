
package oop;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.tree.Visitor;

//note to self, change the createline methods in the layout objects to obsolete, move functionality
//to the printline methods instead, see the notes in each of the append methods in vTableClass

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
		
		while(!(waitqueue.isEmpty())){
			translate(waitqueue.pop());
		}
	}
	
	public void hardIncludeJavaLangObject(){
		vTableClass javaobject = new vTableClass("Object", null);
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
		String[] args = {sourcefile};
		Node sourceAST = new ASTGenerator().generateAST(args);
		
		new Visitor() {
			//class variables
			vTableClass currentclass;
			vTableLayoutLine currentlayout;
			vTableAddressLine currentaddress;
			vTableMethodLayoutLine currentmethod;
			vTableForwardDeclarations forwarddeclarations;
			
			/**
			 * Possible Parents: None
			 * Writes to Elements: No
			 * 
			 * @param n GNode from the parser
			 */
			public void visitCompilationUnit(GNode n){
				visit(n);
			}    		

			/**
			 * Possible Parents: 
			 * Writes to Elements: No
			 * 
			 * covers byte, int, short, long
			 * @param n
			 */
			public void visitIntegerLiteral(GNode n){
				
			}

			/**
			 * Possible Parents: 
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitBooleanLiteral(GNode n){	
				
			}

			/**
			 * Possible Parents:
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitFloatingPointLiteral(GNode n){
				
			}

			/**
			 * Possible Parents:
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitStringLiteral(GNode n){
				
			}
			
			/**
			 * 
			 * @param n
			 */
			public void visitFieldDeclaration(GNode n){
				visit(n);
			}

			/**
			 * Possible Parents: FieldDeclaration, MethodDeclaration
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitVoidType(GNode n){

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
			}

			/**
			 * Possible Parents: Expression
			 * Writes to Elements: No
			 * 
			 * the name of the variable
			 * @param n
			 */
			public void visitPrimaryIdentifier(GNode n){

			}

			/**
			 * Possible Parents: PackageDeclaration, Type
			 * Writes to Elements: No
			 * 
			 * 
			 * @param n
			 */
			public void visitQualifiedIdentifier(GNode n){
				String returntype = n.getString(0);
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
			}

			/**
			 * Possible Parents: ClassDeclaration
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitClassBody(GNode n){

			}

			/**
			 * Possible Parents: ClassBody
			 * Writes to Elements: No
			 * 
			 * @param n
			 */
			public void visitConstructorDeclaration(GNode n){

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
			
			/**
			 * Possible Parents: CompilationUnit
			 * Writes to Elements: includesCC, includesH
			 * 
			 * @param n
			 */
			public void visitImportDeclaration(GNode n){
				
			}
			
			/**
			 * Possible Parents: CompilationUnit
			 * Writes to Elements: namespaceCC, namespaceH
			 * 
			 * 
			 * @param n
			 */
			public void visitPackageDeclaration(GNode n){
								
			}

			/**
			 * Possible Parents: CompilationUnit
			 * Writes to Elements: forwardDeclarationsH, dataLayoutH
			 * 
			 * @param n
			 */
			public void visitClassDeclaration(GNode n){
				
			}

			public void visitArguments(GNode n){
				visit(n);
				
			}
			public void visitMethodDeclaration(GNode n){					

			}
			
			/**
			 * 
			 * @param n
			 */
			public void visit(Node n) {
				for (Object o : n) if (o instanceof Node) dispatch((Node)o);
			}
		}.dispatch(sourceAST);

	}
}	