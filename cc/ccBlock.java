package oop;


/*
 * Current state: Mitigating responsibilities from ccBlock to ccDeclaration and ccExpression. Further functionality going to
 * ccStatement.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;


class ccBlock extends Visitor{
	
	public LinkedList<String> blockLines;
	public HashMap<String, String> variables;
	private LinkedList<String> localVariableNames;
	public LinkedList<ccClass> classList;
	public String currentClass;
	
	public ccBlock(){
		localVariableNames = new LinkedList<String>();
		blockLines = new LinkedList<String>();
	}

	public ccBlock(GNode n, HashMap var, LinkedList<String> parameterNames, LinkedList<ccClass> classes, String currentc) {
		blockLines = new LinkedList<String>();
		localVariableNames = new LinkedList<String>();
		localVariableNames.addAll(parameterNames);
		classList = classes;
		currentClass = currentc;
		variables = var;
		visit(n);
	}
	
	public ccBlock(GNode n) {
		localVariableNames = new LinkedList<String>();
		blockLines = new LinkedList<String>();
		blockLines.add("{");
		visit(n);
		blockLines.add("}");
	}
	
	public void visitFieldDeclaration(GNode n){
		String name = (String)n.getNode(2).getNode(0).getString(0);
		String type = (String)n.getNode(1).getNode(0).getString(0);
		variables.put(name, type);
		localVariableNames.add(name);
		ccDeclaration declarationStatement = new ccDeclaration(n, this);
		blockLines.add(" " + declarationStatement.publish() + "\n");
	}

	
	//TODO: Next step = expression statements and all components
	public void visitExpressionStatement(GNode n){
		ccExpression expressionStatement = new ccExpression(n, this);
		blockLines.add("  " + expressionStatement.publish() + "\n");
	}

	public void visitBlock(GNode n){
//		System.out.println(n);
		ccBlock blockStatement = new ccBlock(n, variables, localVariableNames, classList, currentClass);
		blockLines.add("  {\n");
		blockLines.add("  " + blockStatement.publish());
		blockLines.add("  }\n");
	}


	public void visitConditionalStatement(GNode n){
		ccStatement ifLine = new ccStatement(n);
		blockLines.add("  " + ifLine.line + "\n");
	}
	public void visitForStatement(GNode n){
//		System.out.println(n);
		ccStatement forLine = new ccStatement(n);
		blockLines.add("  " + forLine.line + "\n");
	}
	public void visitBreakStatement(GNode n){
//		System.out.println(n);
		ccStatement breakLine = new ccStatement(n);
		blockLines.add("  " + breakLine.line + "\n");
	}
	public void visitWhileStatement(GNode n){
		ccStatement whileLine = new ccStatement(n);
		blockLines.add("  " + whileLine.line + "\n");
	}
	public void visitReturnStatement(GNode n){
		ccStatement whileLine = new ccStatement(n);
		blockLines.add("  " + whileLine.line + "\n");
	}
		
	public LinkedList<String> getLocalVariables(){
		return localVariableNames;
	}
	
	public void addLine(String s){
		blockLines.add(s);
	}
	
	public LinkedList<String> publish() {
		return blockLines;
	}
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
		}
	}
}
