package oop;


/*
 * Current state: Mitigating responsibilities from ccBlock to ccDeclaration and ccExpression. Further functionality going to
 * ccStatement.
 */

import java.util.LinkedList;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;


class ccBlock extends Visitor{
	
	public LinkedList<String> blockLines;
	
	public ccBlock(){
	}

	public ccBlock(GNode n) {
		blockLines = new LinkedList<String>();
		visit(n);
	}

	public void visitFieldDeclaration(GNode n){
		ccDeclaration declarationStatement = new ccDeclaration(n);
		blockLines.add("  " + declarationStatement.publish() + "\n");
	}

	
	//TODO: Next step = expression statements and all components
	public void visitExpressionStatement(GNode n){
		ccExpression expressionStatement = new ccExpression(n);
		blockLines.add("  " + expressionStatement.publish() + "\n");
	}

	public void visitBlock(GNode n){
//		System.out.println(n);
		ccBlock blockStatement = new ccBlock(n);
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
		blockLines.add("  Return" + whileLine.line + "\n");
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
