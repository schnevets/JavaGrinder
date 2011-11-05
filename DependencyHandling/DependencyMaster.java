package Trans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import xtc.parser.ParseException;
import xtc.parser.Result;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

import xtc.lang.JavaFiveParser;

/**
 * A tool to deal with dependencies when translating files from Java to C++
 * -Finds all the classes referenced within the argument file
 * -If these classes have .java files of the same name in the package, adds them to a HashSet
 * -Returns this HashSet to the Translator master class
 *
 * @author Justin Bernegger
 * @version 1.0
 */

public class DependencyMaster extends xtc.util.Tool {
	
	HashSet<String> filesToTranslate;
	
	public DependencyMaster() {
	}
	
	public String getName() {
		return "Dependency Master";
	}
	
	public String copy(){
		return "Justin Bernegger";
	}
	
	public Set<String> getDependencies(String s){
		filesToTranslate = new HashSet<String>();
		filesToTranslate.add(s);
		try {
			process(s);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return filesToTranslate;
	}
	
	public Node parse(Reader in, File file) throws IOException, ParseException {
		JavaFiveParser parser =
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}

	public void process(Node node) {
		new Visitor() {
			
			//If the qualified identifier is a class within the package, add its absolute path to filesToTranslate
			public void visitQualifiedIdentifier(GNode n){
				String fileName = n.getString(0)+".java";
				try {
					filesToTranslate.add(locate(fileName).getAbsolutePath());
				} catch (FileNotFoundException e) {
					visit(n);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void visit(Node n) {
				for(Object o : n) if (o instanceof Node){
					dispatch((Node)o);
				}
			}
			
		}.dispatch(node);
	}
}

