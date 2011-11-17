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

public class ccTest extends xtc.util.Tool {

	private String outFileName = "";

	/** Create a new translator. */
	public ccTest() {
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

	public void process(Node node) {
		if (runtime.test("printJavaAST")) {
			runtime.console().format(node).pln().flush();
		}

		if (runtime.test("translate")) {
			ccMaster ccm = new ccMaster();
			ccm.dispatch(node);
			try{
				ccm.publishToFiles();
			} catch (IOException e){
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Run the translator with the specified command line arguments.
	 *
	 * @param args The command line arguments.
	 */
	public static void main(String[] args) {
		new ccTest().run(args);
	}

}