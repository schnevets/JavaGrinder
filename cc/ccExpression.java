package oop;

import xtc.tree.GNode;
import xtc.tree.Visitor;

public class ccExpression extends Visitor{
	//Expression statements are any single lines within a java method... i think
	String line;
	
	public ccExpression(GNode n){
		System.out.println(n);
		dispatch((GNode)n.get(0));
	}
	
	public void visitExpression(GNode n){
		line = new ccStatement(n).publish();
		System.out.println(line);					//line should be added to ccBlock
	}
	public void visitCallExpression(GNode n){
		line = new ccStatement(n).publish();
		System.out.println(line);					//line should be added to ccBlock
	}

}
