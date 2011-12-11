package oop;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class ccExpression extends Visitor{
	//Expression statements are any single lines within a java method... i think
	String line;
	
	public ccExpression(GNode n){
		dispatch((GNode)n.get(0));
	}
	
	public void visitExpression(GNode n){
		line = new ccStatement(n).publish();
	}
	public void visitCallExpression(GNode n){
		line = new ccStatement(n).publish();
	}
	public void visitPostfixExpression(GNode n){
		line = new ccStatement(n).publish();
	}
	
	public String publish(){
		return line;
	}

}
