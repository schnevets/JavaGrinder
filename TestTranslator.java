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
package xtc.oop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

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
    JavaFiveParser parser =
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
    		
    		private List<String> includesCC;
    		private List<String> nameSpaceCC;
    		private List<String> methodCC;
    		private List<String> vTableDefCC;
    		
    		private List<String> includesH;
    		private List<String> nameSpaceH;
    		private List<String> forwardDeclarationsH;
    		private List<String> constructorH;
    		private List<String> dataLayoutH;
    		private List<String> methodsImplementedH;
    		private List<String> vTableH;
    		private List<String> vTableLayoutH;


		
		//the source directory for the .java files
    		private String basedirectory = "/home/tev/workspace/OOPHomework1/";
    		
    		private void createFilesAndWriters(){
    			try{
    				fileCC = new File(basedirectory + "Tempfile" + ".cc");
    				fileH = new File(basedirectory + "Tempfile" + ".h");
    				//fileCC.createNewFile();
    				//fileH.createNewFile();

    				includesCC=new LinkedList();
    	    		nameSpaceCC=new LinkedList();
    	    		methodCC=new LinkedList();
    	    		vTableDefCC=new LinkedList();
    	    		
    	    		includesH=new LinkedList();
    	    		nameSpaceH=new LinkedList();
    	    		forwardDeclarationsH=new LinkedList();
    	    		constructorH=new LinkedList();
    	    		dataLayoutH=new LinkedList();
    	    		methodsImplementedH=new LinkedList();
    	    		vTableH=new LinkedList();
    	    		vTableLayoutH=new LinkedList();
   					
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
    		
    		private void assembleElement(List Element, BufferedWriter file){
    			while(!Element.isEmpty()){
    				try {
						file.write((String)Element.remove(0)+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
    			}
    		}
    		
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

    		public void visitClassDeclaration(GNode n){
    			
    			File tempfile1 = new File(basedirectory + ((String)n.get(1)) + ".cc");
    			File tempfile2 = new File(basedirectory + ((String)n.get(1)) + ".h");
    			if(fileCC.renameTo(tempfile1)){
    				fileCC = tempfile1;
    			}
    			if(fileH.renameTo(tempfile2)){
    				fileH = tempfile2;
    			}
    			
    			String testname = "javatest";
    			
    			String sprfilename = new String(basedirectory + testname + ".cc");
    			File argstest = new File(sprfilename);
    	    			
    			
				System.out.println(fileCC.getPath());
    			visit(n);
				if(!argstest.exists()){
    				System.out.println("We are in here!");
					String[] args = new String[2];
    				args[0] = ("-translate");
    				args[1] = (basedirectory + testname + ".java");
    				TestTranslator trans = new TestTranslator();
    				trans.run(args);
				}    			
    		}
    		
    		public void visit(Node n) {
    			methodCC.add("Dadsa");
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

