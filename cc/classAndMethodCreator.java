package oop;

import java.util.LinkedList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;
import xtc.util.Pair;

public class classAndMethodCreator extends Visitor {
	
	ccClass currentClass;
	LinkedList<ccClass> classList;
	LinkedList<String> modifierList;
	String[] argumentType;
	String[] argumentName;
	LinkedList<Object> BlockText;
	ccBlock latestBlock;
	
	public classAndMethodCreator(LinkedList<ccClass> clist){
		classList = clist;
	}
	
	public void visitCompilationUnit(GNode n){
		modifierList = new LinkedList<String>();
		visit(n);
	}
	
	public void visitClassDeclaration(GNode n){
		String name = (String)n.getString(1);
		String access = "public";
		boolean isStatic = false;
		dispatch(n.getNode(0));
		for(int i = 0; i < modifierList.size(); i++){
			if(modifierList.get(i).matches("public|private|protected")){
				access = modifierList.get(i);
			}
			else if(modifierList.get(i).matches("static")){
				isStatic = true;
			}
		}
		modifierList.clear();
		classList.add(new ccClass(name, access, isStatic));
		currentClass = classList.getLast();
		visit(n);
	}
	
	public void visitConstructorDeclaration(GNode n){
		String name = (String)n.getString(2);
		String access = "public";
		dispatch(n.getNode(0));
		for(int i = 0; i < modifierList.size(); i++){
			if(modifierList.get(i).matches("public|private|protected")){
				access = modifierList.get(i);
			}
		}
		modifierList.clear();
		Node param = n.getNode(3);
		argumentType = new String[param.size()];
		argumentName = new String[param.size()];
		for(int i = 0; i < param.size(); i++){
			argumentType[i] = param.getNode(i).getNode(1).getNode(0).getString(0);
			argumentName[i] = param.getNode(i).getString(3);
		}
		currentClass.addConstructor(new ccConstructor(name, access, argumentType, argumentName));
	}
	
	public void visitMethodDeclaration(GNode n){
		String name = (String)n.getString(3);
		String access = "public";
		String returnType = "void";
		boolean isStatic = false;
		dispatch(n.getNode(0));
		for(int i = 0; i < modifierList.size(); i++){
			if(modifierList.get(i).matches("public|private|protected")){
				access = modifierList.get(i);
			}
			else if(modifierList.get(i).matches("static")){
				isStatic = true;
			}
		}
		modifierList.clear();
		if(n.getNode(2).hasName("VoidType")){ /* nope, we already good */}
		else{
			returnType = n.getNode(2).getNode(0).getString(0);
		}
		
		Node param = n.getNode(4);
		argumentType = new String[param.size()];
		argumentName = new String[param.size()];
		for(int i = 0; i < param.size(); i++){
			argumentType[i] = param.getNode(i).getNode(1).getNode(0).getString(0);
			argumentName[i] = param.getNode(i).getString(3);
		}
		visit(n);							//After the method's meta-info is collected, n visits the block, where the "guts" are assembled"
		currentClass.addMethod(new ccMethod(name, currentClass, access, returnType, argumentType, argumentName, isStatic, latestBlock));
	}
	
	public void visitModifier(GNode n){
		for(Object s: n){
			if (s instanceof String)
				modifierList.add((String)s);
		}
	}

	
	/**visitBlock
	 * A block is always visited after a method. This class creates a ccBlock, which will eventually 
	 * consist of the method's "guts" and be added to the ccMethod in the form of a list or other straightforward
	 * structure
	 * 
	 * Problem: visitBlock will assemble cc code, but we need to assign that block to the current method. The quick solution is to 
	 * add a parameter to addMethod. Anyone have any other insight?
	 */
	public void visitBlock (GNode n){
		latestBlock = new ccBlock(n);
//		BlockText = new LinkedList<Object>();		
	}
	
	public void visit(Node n) {
		for (Object o : n) if (o instanceof Node) dispatch((Node)o);
	}
}