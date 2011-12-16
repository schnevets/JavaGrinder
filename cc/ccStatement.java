package oop;

import oop.ccBlock;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ccStatement extends Visitor{
	String line="";
	private ccBlock block;
	
	public ccStatement(GNode n, ccBlock parent) {
		block = parent;
		dispatch(n);
	}
	
	public ccStatement(GNode n) {
		dispatch(n);
	}
	public void visitExpression(GNode n){
		visit(n);
	}
	public void visitReturnStatement(GNode n){
		line += "return ";
		visit(n);
		line+=";";
	}
	public void visitCallExpression(GNode n){
		if(n.getString(2).contains("print")){
			line+="std::cout << ";
			dispatch(n.getGeneric(3));
			if(n.getString(2).equals("println")){
				line+=" << std::endl";
			}
		}
		else{
			String objectType = block.currentClass;
			ccMethod methodInQuestion;
			if(null != n.getNode(0) && null != n.getNode(0).getString(0)){
				if(block.variables.containsKey(n.getNode(0).getString(0))){
					objectType = block.variables.get(n.getNode(0).getString(0));
				}
			} 
			for(int i=0; i< block.classList.size(); i++){
				if(block.classList.get(i).getName().startsWith(objectType)){ 
					System.out.println(block.classList.get(i).getMethodName(n.getString(2), findArgumentTypes(n.getNode(3))));
				}
			}
		}
		visit(n);
	}
	
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
	
	public void visitBlock(GNode n){
		ccBlock blockStatement = new ccBlock(n);
		while (!blockStatement.blockLines.isEmpty())
			line += blockStatement.blockLines.remove()+"\n";
	}
	
	public void visitConditionalStatement(GNode n){
		line += "if("+new ccStatement((GNode)n.get(0)).publish()+")\n";
		dispatch(n.getNode(1));
		line += "\n else ";
		dispatch(n.getNode(2));
		System.out.println(line);
	}
	public void visitForStatement(GNode n){
		line = "for(";
		dispatch(n.getGeneric(0));
		line+=")\n";
		dispatch(n.getGeneric(1));
	}
	public void visitBasicForControl(GNode n){
		dispatch(n.getNode(0));
		dispatch(n.getNode(1));
		dispatch(n.getNode(2));
		dispatch(n.getNode(3));
		line+=";";
		dispatch(n.getNode(4));
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
	}
	public void visitBitwiseOrExpression(GNode n){
		dispatch(n.getNode(0));
		line+="|";
		dispatch(n.getNode(1));
	}
	public void visitBitwiseAndExpression(GNode n){
		dispatch(n.getNode(0));
		line+=" & ";
		dispatch(n.getNode(1));
	}
	public void visitExpressionStatement(GNode n){
		visit(n);
		line+=";";
	}
	public void visitArguments(GNode n){
		line += "(";
		int i = 0;
		for (Object o : n){
			if(i > 0){line += ", ";}
			if (o instanceof Node){
				dispatch((Node)o);
			}
			i++;
		}
		line += ")";
	}
	
	public void visitSelectionExpression(GNode n){
		visit(n);
	}
	public void visitThisExpression(GNode n){
		line+="this->";
	}
	public void visitPrimaryIdentifier(GNode n){
		if(block!=null&&!block.getLocalVariables().contains(n.get(0))){
			line+=(String) "__this::" + n.get(0)+" ";
		}	

		else{
			line+=(String) n.get(0)+" ";
		}
	}
	
	public void visitNewClassExpression(GNode n){
		line+= "__";
		visit(n);
	}
	
	public  String publish() {
		return line;
	}

	public String[] findArgumentTypes(Node n){
		String[] argTypes = new String[n.size()];
		for(int i=0; i< n.size(); i++){
			if(n.getNode(i).getName().matches("IntegerLiteral")){
				System.out.println("int32_t");
				argTypes[i] = "int32_t";
			}
			else if(n.getNode(i).getName().matches("FloatingPointLiteral")){
				System.out.println("double");
				argTypes[i] = "double";
			}
			else if(n.getNode(i).getName().matches("StringLiteral")){
				System.out.println("String");
				argTypes[i] = "String";
			}
			else if(n.getNode(i).getName().matches("BooleanLiteral")){
				System.out.println("bool");
				argTypes[i] = "bool";
			}
			else if(n.getNode(i).getName().matches("ThisExpression")){
				System.out.println(block.currentClass);
				argTypes[i] = block.currentClass;
			}
			else if(n.getNode(i).getName().matches("PrimaryIdentifier")){
				System.out.println("!!!:" + n.getNode(i).getString(0));
				argTypes[i] = block.variables.get(n.getNode(i).getString(0));
			}
		}
		return argTypes;
	}
	
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
			else if(o instanceof String){
				line+=o+"";
			}
		}
	}
}
