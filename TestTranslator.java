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
    return "Ariel Bendon, Christopher Lee, Daniel Uebelein, Justin Bernegger, & Steven Socha";
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
    		
    		private String ccstring;
    		private String hstring;
    		private boolean hflag;
    		
    		//global string variable for assembling each line
    		//each line counts as an element in the linekd list
    		//at some higher level the completed line gets pushed onto the list, global string gets cleared
    		//making way for next line, example would be at the expressionstatement level the string would be pushed
    		//the lower expressions would just append the global string

		
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
    		
    		/**
    		 * Visiting the Compilation Unit, which is parent of everything.
    		 * 
    		 * @param n It's the Node, genius.
    		 */
    		public void visitCompilationUnit(GNode n){
    			createFilesAndWriters();
    			try {
    				outH.append("#pragma once");
					outH.newLine();
					outH.flush();
				} 
    			catch (IOException e) {
					e.printStackTrace();
				}
    			visit(n);
    			assembleFile();
    		}    		

    	//covers byte, int, short, long
		public void visitIntegerLiteral(GNode n){
			System.out.print(n.getString(0));
		}
		
		public void visitBooleanLiteral(GNode n){	
			System.out.print(n.getString(0));	
		}
		
		//covers doubles and floats
		public void visitFloatingPointLiteral(GNode n){
			//System.out.println("Double Literal Test: " + n.getString(0));
			System.out.print(n.getString(0));
		}
		
		public void visitStringLiteral(GNode n){
			//System.out.println("String Literal Test: " + n.getString(0));
			System.out.print(n.getString(0));
		}
	
		//handles addition and subtraction
		public void visitAdditiveExpression(GNode n){
			ExpressionHandler(n);
			//System.out.print("\n");
		}
		
		//handles multiplication, division and modulus
		public void visitMultiplicativeExpression(GNode n){
			ExpressionHandler(n);
		}
		
		//Single Expression Statement
		public void visitExpressionStatement(GNode n){
			visit(n);
			System.out.print(";\r");
		}
		
		//Standard Expression
		public void visitExpression(GNode n){
			ExpressionHandler(n);
		}
		
		//Primary Handler for all Expression nodes
		public void ExpressionHandler(GNode n){
			for(Object o: n){
				if (o instanceof Node){
					dispatch((Node)o);
				}
				else{
					if(o != null)
					System.out.print(((String)o).toString());
				}
			}
		}
		
		public void visitPrimaryIdentifier(GNode n){
			System.out.print(n.getString(0));
		}
		
		public void visitModifier(GNode n){
			//System.out.println("Modifier Test: " + n.getString(0) + " ");
			System.out.print(n.getString(0) + " ");
		}
		
		public void visitDeclarator(GNode n){
			System.out.print(n.getString(0));
			//if(n.get(1) != null) visit(n.getNode(1));
			System.out.print("=");
			visit(n);
			System.out.print(";\r");
		}
		
    		/**
    		 * Visiting a Class Declaration.
    		 * 
    		 * @param n It's the Node, smarty.
    		 */
    		public void visitClassDeclaration(GNode n){

//    			String sprfilename = new String(basedirectory + outFileName + ".cc");
//    			File argstest = new File(sprfilename);
//    	    			
//    			
//				System.out.println(fileCC.getPath());
    			visit(n);
//				if(!argstest.exists()){
//    				System.out.println("We are in here!");
//					String[] args = new String[2];
//    				args[0] = ("-translate");
//    				args[1] = (basedirectory + outFileName + ".java");
//    				TestTranslator trans = new TestTranslator();
//    				trans.run(args);
//				}    			
    		}
    		
    		/** visiting... anything else? Dadsa! */
    		public void visit(Node n) {
    			//methodCC.add("Dadsa");
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

