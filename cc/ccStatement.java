package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ccStatement extends Visitor{
	String line;
	
	public ccStatement(GNode n) {
		dispatch(n);
	}
	
//	public void visitConditionalStatement(GNode n){
//		System.out.println(n);
//	}

	public void visitIntegerLiteral(GNode n){
		line=(String) n.get(0);
	}
	public void visitFloatingPointLiteral(GNode n){
		line=(String) n.get(0);
	}
	public void visitStringLiteral(GNode n){
		line=(String) n.get(0);
	}
	
	public  String publish() {
		return line;
	}

	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
		}
	}
}
