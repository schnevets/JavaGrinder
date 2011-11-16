package oop;

import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;


class ccBlock extends Visitor{

	String newline;
	Boolean equalsUsed=false;
	

	public ccBlock(GNode n) {
		visit(n);
	}

	public void visitFieldDeclaration(GNode n){
		newline = "";
		equalsUsed=false;
//		System.out.println("THIS IS THE START "+n);
		visit(n);
		newline+=";";
		System.out.println(newline);
	}

	
	//TODO: Next step = expression statements and all components
	public void visitExpressionStatement(GNode n){
//		System.out.println(n);
	}
	
//Currently at work. Works with boolean literals.
	public void visitConditionalStatement(GNode n){
//		newline = "If(";
//		visit(n);
//		newline += ")";
//		newline = "";
	}
	
	public void visitWhileStatement(GNode n){
//		newline = "While(";
//		System.out.println(newline);
//		visit(n);
////		newline += ")";
//		System.out.println("HAY THIS IS A WHILE!!!      "+ newline);		
//		newline = "";
	}
	
	public void visitBreakStatement(GNode n){
		visit(n);

//		System.out.println("Visiting break ");
	}
	
	public void DoWhileStatement(GNode n){
		visit(n);
//		System.out.println("Visiting do while");
	}
	
	public void visitCallExpression(GNode n){
		visit(n);
//		System.out.println("Expression called "+n);
	}
	
	
	
	
	public void visitBooleanLiteral(GNode n){
//		newline+=n.get(0);
//		System.out.println(n.get(0));
//		System.out.println("HAY TAKE A LOOK HERE!!!      "+ newline+")");
//		newline = "";
	}
	
	public void visitModifiers(GNode n){
		for(int k = 0; k<n.size();k++){
//			newline+=n.get(k);
		}
	}
	
	public void visitType(GNode n){
		visit(n);
//		for(int k = 0; k<n.size();k++){
//			newline+=n.get(k);
//		}
	}	
	
	//Should be complete
	public void visitDeclarator(GNode n){
		visit(n);
	}
	
	//All of the literals are very similar. Does anyone know of a way to avoid repetition?
	public void visitIntegerLiteral(GNode n){
		concatEquals();
		newline+=n.getString(0);		
	}
	public void visitFloatingPointLiteral(GNode n){
		concatEquals();
		newline+=n.getString(0);
	}
	public void visitStringLiteral(GNode n){
		concatEquals();
		newline+=n.getString(0);
	}
	
	public void visitAdditiveExpression(GNode n){
		concatEquals();
		visit(n);
	}
	
	public void visitNewClassExpression(GNode n){
		concatEquals();
		visit(n);
	}
	
//	public void visitPrimitiveType(GNode n){
//		extract(n);
//	}
//	public void visitQualifiedIdentifier(GNode n){
//		extract(n);
//	}

	public void visitPrimaryIdentifier(GNode n){
		visit(n);
	}
	
	public void visitArguments(GNode n){
		newline+="(";
		visit(n);
		newline+=")";
	}
	
	public void concatEquals(){
		if(!equalsUsed){
			newline+= "= ";
			equalsUsed=true;
		}
	}
	
	public void extract(Node n){
		for(Object s: n){
			if (s instanceof String)
				newline+=s+" ";
		}
		visit(n);
	}	
	
	//Modified visit to take any instances of Strings and append them to the line.
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
			if (o instanceof String){
				newline+=o+" ";				
			}
		}
	}
}
