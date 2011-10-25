/*
 * xtc - The eXTensible Compiler
 * Copyright (C) 2011 Robert Grimm
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */
package oop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.lang.JavaFiveParser;

/**
 * A translator from (a subset of) Java to (a subset of) C++.
 *
 * @author Robert Grimm
 * @version $Revision$
 */

public class TestTranslator extends xtc.util.Tool {

	private String outFileName = "";

	/** Create a new translator. */
	public TestTranslator() {
		// Nothing to do.
	}

	public String getName() {
		return "JavaGrinder - A Java to C++ Translator";
	}

	public String getCopy() {
		return "Christopher Lee, Daniel Uebelein, Justin Bernegger, & Steven Socha";
	}

	public void init() {
		super.init();

		runtime.
		bool("printJavaAST", "printJavaAST", false, "Print Java AST.").
		bool("countMethods", "countMethods", false, "Count all Java methods.").
		bool("translate", "translate", false, "Translate a Java file to a C++ file.");

	}

	public Node parse(Reader in, File file) throws IOException, ParseException {
		outFileName = file.getName().substring(0, file.getName().indexOf('.'));		// This is the only place I could find where I could save the name of the argument file.
		JavaFiveParser parser =														// But I'm ignorant and there's probably a more elegant way of getting it.
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}

	/*
	 * Existing Issues
	 * - Not all visitors created so the printing of each line (cc or h) will not be perfect at all, predict outputs using AST before testing
	 * - Formatting will be a constant issue as we add in more visitors, debug and check outputs often
	 * - primitive types are not c++ 32bit versions (ex int32_t), have to change that
	 * - most of the formatting to c++ has not been done yet
	 */
	public void process(Node node) {
		if (runtime.test("printJavaAST")) {
			runtime.console().format(node).pln().flush();
		}

		if (runtime.test("translate")) {
			new Visitor() {
				private FileWriter writerCC;
				private FileWriter writerH;
				private File fileCC;
				private File fileH;
				private BufferedWriter outCC;
				private BufferedWriter outH;
				private File java_langH;

				private LinkedList<String> includesCC;
				private LinkedList<String> nameSpaceCC;
				private LinkedList<String> methodCC;
				private LinkedList<String> vTableDefCC;

				private LinkedList<String> includesH;
				private LinkedList<String> nameSpaceH;
				private LinkedList<String> forwardDeclarationsH;
				private LinkedList<String> constructorH;
				private LinkedList<String> dataLayoutH;
				private LinkedList<String> methodsImplementedH;
				private LinkedList<String> vTableH;
				
				private LinkedList<vTableLayoutLine> OvTableLayoutH;
				private LinkedList<vTableAddressLine> OvTableAddressH;
				private LinkedList<vTableLayoutLine> vTableLayoutH;
				private LinkedList<vTableAddressLine> vTableAddressH;
				private LinkedList<String> classTracker;  //tracks what classes have been visited, not used yet

				private LinkedList<String> ccstring;
				private LinkedList<String> hstring;

				private boolean hflag;
				
				private int cccount;
				
				private String marginSpaceCC;
				private String marginSpaceH;
				
				private String className;


				//the source directory for the .java files
				private String basedirectory = "./";	

				/** What it says on the tin.*/
				private void createFilesAndWriters(){
					try{
						java_langH = new File(basedirectory + "java_lang.h");
						
						fileCC = new File(basedirectory + outFileName + ".cc");
						fileH = new File(basedirectory + outFileName + ".h");
						fileCC.createNewFile();
						fileH.createNewFile();

						includesCC = new LinkedList<String>();
						includesCC.add("#include \""+outFileName+".h\";");
						nameSpaceCC = new LinkedList<String>();
						methodCC = new LinkedList<String>();
						vTableDefCC = new LinkedList<String>();

						includesH = new LinkedList<String>();
						nameSpaceH = new LinkedList<String>();
						forwardDeclarationsH = new LinkedList<String>();
						constructorH = new LinkedList<String>();
						dataLayoutH = new LinkedList<String>();
						methodsImplementedH = new LinkedList<String>();
						vTableH = new LinkedList<String>();
						OvTableLayoutH = new LinkedList<vTableLayoutLine>();
						OvTableAddressH = new LinkedList<vTableAddressLine>();
						vTableLayoutH = new LinkedList<vTableLayoutLine>();
						vTableAddressH = new LinkedList<vTableAddressLine>();
						
						writerCC = new FileWriter(fileCC);
						writerH = new FileWriter(fileH);
						outCC = new BufferedWriter(writerCC);
						outH = new BufferedWriter(writerH);

						ccstring = new LinkedList<String>();
						hstring = new LinkedList<String>();

						marginSpaceH = "  ";
						marginSpaceCC = "  ";
						readJavaLang();
					}
					
					catch (IOException e){
						e.printStackTrace();
						System.out.println("Something's up with the files and writers. Bitch.");
					}
				}

				/** Final assembly of .cc and .h files */
				private void assembleFile(){
					assembleElement(includesCC,outCC);
					assembleElement(nameSpaceCC,outCC);
					assembleElement(methodCC,outCC);
					assembleElement(vTableDefCC,outCC);
					try {
						outCC.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					assembleElement(includesH,outH);
					assembleElement(nameSpaceH,outH);
					assembleElement(forwardDeclarationsH,outH);
					assembleElement(dataLayoutH,outH);
					assembleElement(constructorH,outH);
					assembleElement(methodsImplementedH,outH);
					assembleElement(vTableH,outH);
					//assembleVTable(vTableLayoutH,vTableAddressH,outH);											//Will need it's own implementation to handle LinkedList<LinkedList> and insertion location details
					try {
						outH.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				/** 
				 * Aseembles elements from list into appropriate file. 
				 * 
				 * @param Element List of elements.
				 * @param file File aseembled to (outH or outCC).
				 */
				private void assembleElement(LinkedList<String> Element, BufferedWriter file){
					while(!Element.isEmpty()){
						try {
							String vtablesearch = Element.peek();
							file.write((String)Element.remove(0)+"\n");
							if (vtablesearch.contains("//vTableStart")){
								assembleVTable(file);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				private void assembleVTable(BufferedWriter file){
					String classsearch = vTableLayoutH.peek().vTableClass;
					while(!vTableLayoutH.isEmpty()){
						String classcheck = vTableLayoutH.peek().vTableClass;
						if(classcheck.equals(classsearch)){
							vTableLayoutLine line = vTableLayoutH.pop();
							try {
								file.write(line.vTableLine);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							break;
						}
					}
					while(!vTableAddressH.isEmpty()){
						String classcheck = vTableAddressH.peek().vTableClass;
						if(classcheck.equals(classsearch)){
							vTableAddressLine line = vTableAddressH.pop();
							try {
								file.write(line.vTableLine);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							break;
						}
					}
					
				}

				private void addStringsToList(GNode n){
					for(Object s: n){
						if (s instanceof String){
							if (hflag == true){
								hstring.add(((String)s));
							}
							else{
								ccstring.add(((String)s));
							}
						}
					}
				}
				
				/** 
				 * Reads java_lang.h and assembles java.lang.Object vtable in a LinkedList format
				 * 
				 */
				public void readJavaLang(){
					/*
					try {
						Scanner scan = new Scanner(java_langH);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					*/
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
					/*
					while(!(OvTableLayoutH.isEmpty())){
						vTableLayoutLine current = OvTableLayoutH.pop();
						System.out.print(current.vTableLine);
					}
					while(!(OvTableAddressH.isEmpty())){
						vTableAddressLine current = OvTableAddressH.pop();
						System.out.print(current.vTableLine);
					}
					*/
				}
				
				public LinkedList<vTableLayoutLine> convertVLayoutPrelim(LinkedList<vTableLayoutLine> cloneOL, String classname){
					LinkedList<vTableLayoutLine> newclone = new LinkedList<vTableLayoutLine>();
					while(!(cloneOL.isEmpty())){
						vTableLayoutLine current = cloneOL.pop();
						current.setReferenceParameter(classname);
						current.setVTableClass(classname);
						current.createVTableLine();
						newclone.add(current);
					}
					
					//testing
					/*
					while(!(newclone.isEmpty())){
						vTableLayoutLine current = newclone.pop();
						System.out.print(current.vTableLine);
					}
					*/
					return newclone;
				}
				
				//in progress conversion of addresslines to the right reference types
				public LinkedList<vTableAddressLine> convertVTableAddress(vTableAddressLine cloneO, String classname){
					LinkedList<vTableAddressLine> newcloneO = new LinkedList<vTableAddressLine>();
					
					//rework this method entirely, convert one address line only
					/*
					while(!(cloneO.isEmpty())){
						vTableAddressLine current = cloneO.pop();
						current.setClassname(classname);
					}
					*/
					
					return newcloneO;
				}
				
				/**
				 * Possible Parents: None
				 * Writes to Elements: No
				 * 
				 * @param n GNode from the parser
				 */
				public void visitCompilationUnit(GNode n){
					createFilesAndWriters();
					visit(n);
					assembleFile();
				}    		

				/**
				 * Possible Parents: 
				 * Writes to Elements: No
				 * 
				 * covers byte, int, short, long
				 * @param n
				 */
				public void visitIntegerLiteral(GNode n){
					addStringsToList(n);
				}

				/**
				 * Possible Parents: 
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitBooleanLiteral(GNode n){	
					addStringsToList(n);
				}

				/**
				 * Possible Parents:
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitFloatingPointLiteral(GNode n){
					addStringsToList(n);
				}

				/**
				 * Possible Parents:
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitStringLiteral(GNode n){
					addStringsToList(n);
				}

				/**
				 * Possible Parents:
				 * Writes to Elements: No
				 * 
				 * handles addition and subtraction
				 * @param n
				 */
				public void visitAdditiveExpression(GNode n){
					ExpressionHandler(n);
				}

				/**
				 * Possible Parents:
				 * Writes to Elemenst: No
				 * 
				 * handles multiplication, division and modulus
				 * @param n
				 */
				public void visitMultiplicativeExpression(GNode n){
					ExpressionHandler(n);
				}

				/**
				 * Possible Parents: Block
				 * Writes to Elements: methodCC
				 * 
				 * @param n
				 */
				public void visitExpressionStatement(GNode n){
					visit(n);
					if (hflag == true){
						hstring.add(";\r");
					}
					else{
						ccstring.add(";\r");
					}
					String s = "";
					while(hstring.size()!=0){
						s.concat(hstring.pop());
					}
				}

				/**
				 * Possible Parents: ExpressionStatement
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitExpression(GNode n){
					ExpressionHandler(n);
				}

				/**
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void ExpressionHandler(GNode n){
					for(Object o: n){
						if (o instanceof Node){
							dispatch((Node)o);
						}
					}
					addStringsToList(n);
				}

				/**
				 * Possible Parents: ClassBody
				 * Writes to Elements: dataLayoutH
				 * 
				 * @param n
				 */
				public void visitFieldDeclaration(GNode n){
					visit(n);
					if (hflag == true){
						String hLine = marginSpaceH;
						while(hstring.size()>0){
							hLine = hLine + hstring.pop();
						}
						hLine = hLine + ";";
						dataLayoutH.add(hLine);
					}
					

				}

				/**
				 * Possible Parents: FieldDeclaration, MethodDeclaration
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitVoidType(GNode n){
					if (hflag == true){
						hstring.add("void ");
					}
					else{
						ccstring.add("void ");
					}
				}
				
				/**
				 * Possible Parents: FieldDeclaration
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitType(GNode n){
					visit(n);
					if (hflag == true){						//adding a space after the type name
						hstring.add(" ");
					}
					else{
						ccstring.add(" ");
					}
				}

				/**
				 * Possible Parents: Type
				 * Writes to Elements: No
				 * 
				 * primitive type specific, ex int, double, float, etc.
				 * @param n
				 */
				public void visitPrimitiveType(GNode n){
					addStringsToList(n);
				}

				/**
				 * Possible Parents: Expression
				 * Writes to Elements: No
				 * 
				 * the name of the variable
				 * @param n
				 */
				public void visitPrimaryIdentifier(GNode n){
					addStringsToList(n);
				}

				/**
				 * Possible Parents: PackageDeclaration, Type
				 * Writes to Elements: No
				 * 
				 * 
				 * @param n
				 */
				public void visitQualifiedIdentifier(GNode n){
					int i = 0;
					while (i < n.size()){
						if (hflag == true){
							if(0<i && i<n.size()){
								hstring.add(".");
							}
							hstring.add(((String)n.getString(i)));
						}
						else{
							if(0<i && i<n.size()){
								ccstring.add(".");
							}
							ccstring.add(((String)n.getString(i)));
						}
						i++;
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
					if (hflag == false){
						visit(n);
					}
				}

				/**
				 * Possible Parents: Modifiers
				 * Writes to Elements: No
				 * 
				 * public, private, protected, abstract, static, etc.
				 * @param n
				 */
				public void visitModifier(GNode n){
					addStringsToList(n);	
					if (hflag == true){
						hstring.add(" ");
					}
					else{
						ccstring.add(" ");
					}
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
					if(hflag == true){
						hstring.add(" ");
						hstring.add(n.getString(0)); 
					}
					else{
						ccstring.add(n.getString(0));
						if(n.hasVariable())					//check for other children, if has children then =, else nothing
							ccstring.add("=");				
						visit(n);
					}
				}

				/**
				 * Possible Parents: ClassBody
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitBlock(GNode n){
					visit(n);
				}

				/**
				 * Possible Parents: ClassDeclaration
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitClassBody(GNode n){
					for(Object o: n){
						if (o instanceof Node){
							if(((GNode)o).getName().equals("FieldDeclaration")){
								hflag = true;
							}
							else{
								hflag = false;
							}
							dispatch((Node)o);

						}
					}
				}

				/**
				 * Possible Parents: ClassBody
				 * Writes to Elements: No
				 * 
				 * @param n
				 */
				public void visitConstructorDeclaration(GNode n){
					hflag = true;
					dispatch((Node)n.getNode(3));
					String hLine;
					hLine = marginSpaceH + "__" + className;
					while (hstring.size()>0){
						hLine = hLine + hstring.pop();
					}
					hLine = hLine + ";";
					constructorH.add(hLine);
					hflag = false;
					visit(n);
				}
				
				/**
				* Possible Parents:
				* Writes to Elemenst: No
				*
				* handles multiplication, division and modulus
				* @param n
				*/
				public void visitPostfixExpression(GNode n){
					ccstring.clear();
				}

				/**
				 * Possible Parents: ConstructorDeclaration, MethodDeclaration
				 * Writes to Elements: No
				 * 
				 * method arguments
				 * @param n
				 */
				public void visitFormalParameters(GNode n){
					if (hflag == true){
						hstring.add("(");
						int i = 0;
						for(Object o: n){
							if (o instanceof Node){
								dispatch((Node)o);
							}
							i++;
							if (i < n.size()){
								hstring.add(",");
							}
						}
						hstring.add(")");
					}
					else{
						ccstring.add("(");
						int i = 0;
						for(Object o: n){
							if (o instanceof Node){
								dispatch((Node)o);
							}
							i++;
							if (i < n.size()){
								ccstring.add(",");
							}
						}
						ccstring.add(")");
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
					addStringsToList(n);
				}
				
				/**
				* Possible Parents: ExpressionStatement, EqualityExpression, AdditiveExpression, Expression
				* Writes to Elements: No
				*
				* @param n
				*/
				public void visitCallExpression(GNode n){
					String ceString = "";
					for(Object o: n){
						if (o instanceof Node){
							Node c = (Node)o;
							if (c.hasName("SelectionExpression")){
								dispatch((Node)o);
							}
						}
					}
					addStringsToList(n);
					int i = cccount;
					while(i < ccstring.size()){
						ceString = ceString + ccstring.get(i) + "::";
						ccstring.remove(i);
					}
					ceString = ceString.substring(0, ceString.length() -2);
					if(ceString.contentEquals("System::out::println") || ceString.contentEquals("System::out::print")){
						ceString = "";
						if(!includesCC.contains("#include <iostream>"))
							includesCC.add("#include <iostream>");
						for(Object o: n){
							if (o instanceof Node){
								Node c = (Node)o;
								if (c.hasName("Arguments")){
									dispatch((Node)o);
								}
							}
						}
						i = cccount;
						while(i < ccstring.size()){
							ceString = ceString + ccstring.get(i);
							ccstring.remove(i);
						}
						ceString = marginSpaceCC + "std::cout << " + ceString + "<< std::endl;\n";
						ccstring.add(ceString);
						cccount++;
					}
					
					visit(n);
				}
				
				/**
				* Only used for System.out.println... for now
				* Possible Parents: CallExpression
				* Writes to Elements: MethodCC (special case)
				*
				* @param n
				*/
				public void visitSelectionExpression(GNode n){
					visit(n);
					addStringsToList(n);
				}

				
				/**
				* Possible Parents: None (Comma)
				* Writes to Elements: MethodCC
				*
				* @param n
				*/
				public void visitConditionalStatement(GNode n){
					hflag=false;
					visit(n);
				}
				
				/**
				* Possible Parents: ConditionalStatement,
				* Writes to Elements: ccLine
				*
				* @param n
				*/
				public void visitEqualityExpression(GNode n){
					visit(n);
					System.out.println(ccstring);
					String condition = ccstring.remove(2);
					String ccLine = "if("+ccstring.pop()+condition+ccstring.pop()+"=="+ccstring.pop()+")";
					System.out.println(ccLine);
					methodCC.add(ccLine);
					ccLine="";
				}
				
				/**
				 * Possible Parents: CompilationUnit
				 * Writes to Elements: includesCC, includesH
				 * 
				 * @param n
				 */
				public void visitImportDeclaration(GNode n){
					String hLine = "";					//Creating the string line for includesH
					hflag = true;						
					hLine = hLine + "#include <";    
					visit(n);			
					while(hstring.size()!=0){
						hLine = hLine + hstring.pop();
					}
					hLine= hLine + ">;";
					includesH.add(hLine);
					hflag = false;

					String ccLine = "";					//Creating the string line for includesCC						
					ccLine = ccLine + "#include <";    
					visit(n);			
					while(ccstring.size()!=0){
						ccLine = ccLine + ccstring.pop();
					}
					ccLine= ccLine + ">;";
					includesCC.add(ccLine);
				}
				
				/**
				 * Possible Parents: CompilationUnit
				 * Writes to Elements: namespaceCC, namespaceH
				 * 
				 * 
				 * @param n
				 */
				public void visitPackageDeclaration(GNode n){
					String hLine = "";
					hflag = true;
					visit(n);
					while(hstring.peekFirst()!=null){
						if(hstring.peekFirst()=="."){
							hstring.pop();
							marginSpaceH = marginSpaceH + "  ";
							hLine = marginSpaceH;
						}
						hLine = hLine + "namespace ";
						hLine = hLine + hstring.pop() + "{";
						nameSpaceH.add(hLine);
					}
					hflag = false;
					
					String ccLine = "";
					visit(n);
					while(ccstring.peekFirst()!=null){
						if(ccstring.peekFirst()=="."){
							ccstring.pop();
							marginSpaceCC = marginSpaceCC + "  ";
							ccLine = marginSpaceCC;
						}
						ccLine = ccLine + "namespace ";
						ccLine = ccLine + ccstring.pop() + "{";
						nameSpaceCC.add(ccLine);
					}									
				}

				/**
				 * Possible Parents: CompilationUnit
				 * Writes to Elements: forwardDeclarationsH, dataLayoutH
				 * 
				 * @param n
				 */
				public void visitClassDeclaration(GNode n){
					
					String hLine;
					className = n.getString(1);
					hLine = marginSpaceH + "struct __" + className + ";";								//Adding 'struct __ClassName;' to forwardDeclarationH
					forwardDeclarationsH.add(hLine);
					hLine = marginSpaceH + "struct __" + className + "_VT;";							//Adding 'struct __ClassName_VT'; to forwardDeclarationH
					forwardDeclarationsH.add(hLine);
					hLine = "";																			//Adding an empty line to forwardDeclarationH
					forwardDeclarationsH.add(hLine);
					hLine = marginSpaceH + "typedef __" + className + "* " + className + ";";			//Adding 'typedef __ClassName* ClassName;' to forwardDeclarationH
					forwardDeclarationsH.add(hLine);

					hLine =  marginSpaceH + "struct __" + className + " {";								//Adding 'struct __ClassName {' to dataLayoutH
					dataLayoutH.add(hLine);
					marginSpaceH = marginSpaceH + "  ";
					hLine = marginSpaceH + "__" + className + "_VT* __vptr;";							//Adding '__ClassName_VT* __vptr; {' to dataLayoutH
					dataLayoutH.add(hLine);

					hLine = marginSpaceH + "static Class __class();";									//Adding 'static Class __class();' to vTableH
					vTableH.add(hLine);
					hLine = marginSpaceH + "static __" + className + "_VT __vtable;";					//Adding 'static __ClassName_VT __vtable;' to vTableH
					vTableH.add(hLine);
					marginSpaceH = marginSpaceH.substring(2);
					hLine = marginSpaceH + "};";
					vTableH.add(hLine);	
					
					hLine = "//vTableStart " + className;
					vTableH.add(hLine);
					hLine = "\r";
					vTableH.add(hLine);
					
					vTableAddressLine liner = new vTableAddressLine();
					vTableLayoutLine line = new vTableLayoutLine();
					line.setVTableClass(className);
					liner.setVTableClass(className);
					line.createStructLine("struct " + "__" + className + "_VT {");
					liner.createStructLine("__" + className + "_VT() \r : ");
					vTableAddressH.add(liner);
					vTableLayoutH.add(line);
					
					LinkedList<vTableLayoutLine> cloneOL = new LinkedList<vTableLayoutLine>();
					LinkedList<vTableAddressLine> cloneOA = new LinkedList<vTableAddressLine>();
					cloneOA.addAll(OvTableAddressH);
					cloneOL.addAll(OvTableLayoutH);
					cloneOL = convertVLayoutPrelim(cloneOL, className);
					//cloneOA = convertVTableAddress(cloneOA, className);
					
					visit(n); 
					
					line = new vTableLayoutLine();
					liner = new vTableAddressLine();
					line.setVTableClass(className);
					liner.setVTableClass(className);
					line.createStructLine("\r");
					liner.createStructLine(" { \r" + marginSpaceH + "} \r" + marginSpaceH + "}; \r");
					vTableAddressH.add(liner);
					vTableLayoutH.add(line);
				}

				public void visitArguments(GNode n){
					visit(n);
					
				}
				public void visitMethodDeclaration(GNode n){					
					 // --- header file --- (nothing here yet, really)
						hflag = true;
						String hLine = "";
						addStringsToList(n);
						String methodName = hstring.pop();
						hstring.clear();
						for(Object o: n){					// I'm making the assumption that the h file won't need to look
							if (o instanceof Node){			// in the block of the method; I could be wrong.
								Node c = (Node)o;			// (but I don't think so)
								if (!c.hasName("Block")){
									dispatch((Node)o);
								}
							}
						}
//						while(!hstring.isEmpty()){
//							hLine = hLine + hstring.pop() + " ";  <---- printing stuff haphazardly, just to look at it
//						}
//						methodsImplementedH.add(hLine);
						
						
					 // --- cc file ---
						hflag = false;
						String ccLine = "";
						ccstring.clear();
						cccount = 0;
						
						// actual method declaration line
						if(methodName == "main"){ 
							ccLine = "int main()";
						}
						else{
							for(Object o: n){
								if (o instanceof Node){
									Node c = (Node)o;
									if (!c.hasName("Block")){
										dispatch((Node)o);
									}
								}
							}
							while(!ccstring.isEmpty()){
								if (ccstring.peekFirst() == "public" 
										|| ccstring.peekFirst() == "private" 
										|| ccstring.peekFirst() == "protected"){
									ccLine = marginSpaceCC.substring(2) + ccstring.pop() + ":\n" + marginSpaceCC;
								}
								else if (ccstring.peekFirst().charAt(0) == '('){
									ccLine = ccLine + methodName + ccstring.pop();
								}
								else{
									ccLine = ccLine + ccstring.pop();
								}
							}
						}
						
						ccLine = ccLine + "{";
						marginSpaceCC.concat("  ");
						methodCC.add(ccLine);
						
						// method block
						ccLine = marginSpaceCC;
						for(Object o: n){
							if (o instanceof Node){
								Node c = (Node)o;
								if (c.hasName("Block")){
									dispatch((Node)o);
								}
							}
						}
						for(int i = 0; i < cccount; i++){
							ccLine = ccLine + ccstring.pop();
						}
						ccLine = ccLine + marginSpaceCC + "return 0;\n" + marginSpaceCC + "}";
						methodCC.add(ccLine);
					}
				
				/**
				 * 
				 * 
				 * @param n
				 */
				public void visit(Node n) {
					for (Object o : n) if (o instanceof Node) dispatch((Node)o);
				}
			}.dispatch(node);
		}
	}

	/**
	 * Run the translator with the specified command line arguments.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		new TestTranslator().run(args);
	}

}

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

class vTableLayoutLine{
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
		vTableLine = returntype + " "; //+ "(*" + methodname + ") " + parameters + "); \r";
		if (methodname.equals("__isa")){
			vTableLine = vTableLine + methodname;
		}
		else{
			vTableLine = vTableLine + "(*" + methodname + ") ";
			vTableLine = vTableLine + "(" + referenceparameter;
			
			if (!(parameters.length() < 2)){
				vTableLine = vTableLine + parameters;
			}
			vTableLine = vTableLine + ")";
		}
		
		vTableLine = vTableLine + "; \r";
	}
}

class vTableAddressLine{
	String vTableClass;
	String methodname;
	String typecast;
	String classname;
	String vTableLine;
	
	public vTableAddressLine(){
		
	}
	
	public void setMethodName(String methodnamable){
		methodname = methodnamable;
	}
	
	public void setTypeCast(String typecastable){
		typecast = typecastable;
	}
	
	public void setClassname(String classnamable){
		classname = classnamable;
	}
	
	public void setVTableClass(String classable){
		vTableClass = classable;
	}
	
	public void createVTableLine(){
		//methodname((anyspecialcasting)&class::methodname);

		if(methodname.equals("__isa")){
			vTableLine = methodname + "(";
			vTableLine = vTableLine + "&" + classname + "::" + "class()" + ")";
		}
		else{
			vTableLine = ", \r" + methodname + "(";
			if(!(typecast == null)){
				vTableLine = vTableLine + "(" + typecast + ")"; 
			}
			vTableLine = vTableLine + "&" + classname + "::" + methodname + ")";
		}
	}
	
	public void createStructLine(String definition){
		vTableLine = definition;
	}
}