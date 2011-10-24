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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

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
				private LinkedList<String> vTableLayoutH;

				private LinkedList<String> ccstring;
				private LinkedList<String> hstring;

				private boolean hflag;
				
				private String marginSpaceCC;
				private String marginSpaceH;
				
				private String className;


				//the source directory for the .java files
				private String basedirectory = "./";	

				/** What it says on the tin.*/
				private void createFilesAndWriters(){
					try{
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
						vTableLayoutH = new LinkedList<String>();

						writerCC = new FileWriter(fileCC);
						writerH = new FileWriter(fileH);
						outCC = new BufferedWriter(writerCC);
						outH = new BufferedWriter(writerH);

						ccstring = new LinkedList<String>();
						hstring = new LinkedList<String>();

						marginSpaceH = "";
						marginSpaceCC = "";
					}
					
					catch (IOException e){
						e.printStackTrace();
						System.out.println("Something's up with the files and writers. Bitch.");
					}
				}

				/** Final aseembly of .cc and .h files */
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
					assembleElement(constructorH,outH);
					assembleElement(dataLayoutH,outH);
					assembleElement(methodsImplementedH,outH);
					assembleElement(vTableH,outH);
					assembleElement(vTableLayoutH,outH);
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
							file.write((String)Element.remove(0)+"\n");
						} catch (IOException e) {
							e.printStackTrace();
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
					s = "";
					while(ccstring.size()!=0){
						s.concat(ccstring.pop());
					}
					includesCC.add(s);
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
						addStringsToList(n);
					}
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
						System.out.println(hLine);
						dataLayoutH.add(hLine);
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
						hstring.add(n.getString(0)); //check for other children, if has children then =, else nothing
						if(n.hasVariable())
							hstring.add("=");
						visit(n);
						hstring.add("; \r");
					}
					else{
						ccstring.add(n.getString(0) + "=");
						visit(n);
						ccstring.add("; \r");
					}
					String s = "";
					while(hstring.size()!=0){
						s.concat(hstring.pop());
					}
					dataLayoutH.add(s);
					s = "";
					while(ccstring.size()!=0){
						s.concat(ccstring.pop());
					}
					methodCC.add(s);
					//System.out.print(n.getString(0));
					//if(n.get(1) != null) visit(n.getNode(1));
					//System.out.print("=");
					//visit(n);
					//System.out.print(";\r");
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
					visit(n);
				}

				/**
				 * Possible Parents: ConstructorDeclaration, MethodDeclaration
				 * Writes to Elements: No
				 * 
				 * method arguments
				 * @param n
				 */
				public void visitFormalParameters(GNode n){
					//add the first parenthesis
					visit(n);
					//add the closing parenthesis and the opening curly brace
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
						addStringsToList(n);
					}
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
					
					
					
					visit(n); 
					
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
