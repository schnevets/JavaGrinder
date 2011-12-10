package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ccStatement extends Visitor{
	String line="";
	
	public ccStatement(GNode n) {
		dispatch(n);
	}
	public void visitExpression(GNode n){
		visit(n);
		line+=";";
	}
	public void visitReturnStatement(GNode n){
		line += "return ";
		visit(n);
		line+=";";
	}
	public void visitCallExpression(GNode n){
		if(n.getString(2).contains("print")){
			line+="cout << ";
			dispatch(n.getGeneric(3));
			if(n.getString(2).equals("println")){
				line+=" << endl";
			}
		}
		else
			visit(n);
		line+=";";
	}
	
//	public void visitConditionalStatement(GNode n){
//		System.out.println(n);
//	}
	
	public void visitMultiplicativeExpression(GNode n){
		visit(n);
	}

	public void visitIntegerLiteral(GNode n){
		line+=(String) n.get(0);
	}
	public void visitFloatingPointLiteral(GNode n){
		line+=(String) n.get(0);
	}
	public void visitStringLiteral(GNode n){
		line+=(String) n.get(0);
	}
	public void visitBooleanLiteral(GNode n){
		line+=(String) n.get(0);		
	}
	
//	public void visitBlock(GNode n){
//		ccBlock blockStatement = new ccBlock(n);
//	}
	public void visitConditionalStatement(GNode n){
		line = "if("+new ccStatement((GNode)n.get(0)).publish()+")";
//		System.out.println(line);
	}
	public void visitForStatement(GNode n){
		line = "for(";
		dispatch(n.getGeneric(0));
		line+=")";
		ccExpression inside = new ccExpression(n.getGeneric(1));					//declaring a ccExpression, will eventually make ccStatement method to construct
	}
	public void visitBasicForControl(GNode n){
		visit(n);
	}
	public void visitBreakStatement(GNode n){
		line = "break;";
	}
	public void visitWhileStatement(GNode n){
		line = "while("+new ccStatement((GNode)n.get(0)).publish()+")";
//		System.out.println(line);
	}
	public void visitDeclarator(GNode n){
		line+=n.getString(0);
		if(n.getNode(1)==null)
			line+="=";
		else
			line+=n.getString(1);
		dispatch(n.getGeneric(2));
		line+=";";
	}
	public void visitRelationalExpression(GNode n){
		visit(n);
		line+=";";
	}
	public void visitArguments(GNode n){
		visit(n);
	}
	
	public void visitSelectionExpression(GNode n){
		visit(n);
	}
	public void visitPrimaryIdentifier(GNode n){
		line+=(String) n.get(0)+" ";		
	}
	
	public  String publish() {
		return line;
	}

	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
			else if(o instanceof String){
				line+=o+" ";
			}
		}
	}
}
