package Trans;



import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;

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
 * -Repeats the process for all files in the HashSet
 * -Once there are no more files to find, returns the HashSet
 *
 * @author Justin Bernegger
 * @version 1.0
 */

/**
 * @author user
 *
 */
public class DependencyMaster extends xtc.util.Tool {
	
	private HashSet<String> filesFound;
	private String fileDirectory;
	

	public DependencyMaster() {
	}
	
	public String getName() {
		return "Dependency Master";
	}
	
	public String copy(){
		return "Justin Bernegger";
	}
	
	/**
	 * @param String fileName, the filename
	 * @return HashSet<String>, set of all the files necessary to be translated for compilation
	 */
	public HashSet<String> getDependencies(String fileName){
		HashSet<String> filesToTranslate = new HashSet<String>();
		fileDirectory = (new File(fileName).getParent() + '/');
		findDependencies(fileName, filesToTranslate);
		return filesToTranslate;
	}
	
	private void findDependencies(String fileName, HashSet<String> set){
		set.add(fileName);
		filesFound = new HashSet<String>();
		try {
			process(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(String t : filesFound) if (!set.contains(t)){
			findDependencies(t, set);
		}
	}
	
	public Node parse(Reader in, File file) throws IOException, ParseException {
		JavaFiveParser parser =
			new JavaFiveParser(in, file.toString(), (int)file.length());
		Result result = parser.pCompilationUnit(0);
		return (Node)parser.value(result);
	}

	public void process(Node node) {
		new Visitor() {
			
			//If the qualified identifier is a class within the original file's directory, add its absolute path to filesFound
			public void visitQualifiedIdentifier(GNode n){
				String fileName = fileDirectory + n.getString(n.size()-1)+".java";
				if(new File(fileName).exists()){
					filesFound.add(fileName);
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

