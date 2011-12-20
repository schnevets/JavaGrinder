package oop;


import oop.ccMethod;
import xtc.tree.GNode;
import xtc.tree.Node;
import xtc.tree.Visitor;

public class ccStatement extends Visitor{
	String line="";
	private boolean assignmentFlag;
	private ccBlock block;
	int clusterCount=1;
	
	public ccStatement(GNode n, ccBlock parent) {
		block = parent;
		dispatch(n);
	}
	
	/**
	 * DON'T USE THIS CONSTRUCTOR OUTSIDE OF ccMASTER
	 */
	public ccStatement(GNode n) {
		dispatch(n);
	}
	
	public void visitExpression(GNode n){
		if (n.get(1).equals("=")){
			assignmentFlag = true;
		}
		visit(n);
	}
	
	public void visitType(GNode n){
		//for arrays
		if((n.get(1) instanceof Node)&&n.getNode(1).hasName("Dimensions")){
			line+="__rt::Ptr<__rt::Array<";
			dispatch(n.getNode(0));
			line+="> >";

		}
		else{
			visit(n);
		}
	}
	public void visitReturnStatement(GNode n){
		line += "return ";
		visit(n);
		line+=";";
	}
	public void visitCallExpression(GNode n){
		if(n.getString(2).matches("print|println")&&n.get(0).toString().contains("SelectionExpression(PrimaryIdentifier(\"System\"), \"out")){
			line+="std::cout";
			if(!n.getNode(3).isEmpty()){
				line+= " << ";
				dispatch(n.getGeneric(3));
			}
			if(n.getString(2).equals("println")){
				line+=" << std::endl";
			}
		}
		else{
			String objectType = block.currentClass;
			String __this = "__this";
			ccMethod methodInQuestion;
			if(null != n.getNode(0)){
				if(n.getNode(0).get(0) instanceof Node && n.getNode(0).getNode(0).getName().equals("CastExpression")){
					visit(n.getNode(0).getNode(0));
				}
				else if(n.getNode(0).get(0) instanceof String){
					boolean classMatch = false;
					for(ccClass iter : block.classList){
						if (iter.getName().equals(n.getNode(0).getString(0))){
							classMatch = true;
							break;
						}
					}
					if(classMatch){
						__this = n.getNode(0).getString(0);
						objectType = n.getNode(0).getString(0);
					}
					else if(block.variables.containsKey(n.getNode(0).getString(0))){ 
						__this = n.getNode(0).getString(0);
						objectType = block.variables.get(__this).getType();
					}
				}
			} 
			for(int i=0; i< block.classList.size(); i++){
				if(block.classList.get(i).getName().startsWith(objectType)){ 
					for(String s : findArgumentTypes(n.getNode(3))){
					}
					methodInQuestion = block.classList.get(i).getMethod(n.getString(2), findArgumentTypes(n.getNode(3)), block.classList);
					if(methodInQuestion.isStatic){
						line+= "__" + objectType + "::" + methodInQuestion.getName() + "(";
					}
					else if(methodInQuestion.access.contentEquals("private")){
						line+= "__" + objectType + "::" + methodInQuestion.getName() + "(" + __this;
					}
					else {
						line+= __this + "->__vptr->" + methodInQuestion.getName() + "(" + __this;
					}
					for(int j = 0; j < n.getNode(3).size(); j++){
						line+= ", ";
						if(n.getNode(3).getNode(j).hasName("StringLiteral")){
							line+="new __String("+n.getNode(3).getNode(j).getString(0)+")";
						}
						else{
							dispatch(n.getNode(3).getNode(j));
						}
					}
					line+= ")";
				}
			}
		}
	}
	public void visitCastExpression(GNode n){
		line+="(";
		dispatch(n.getNode(0));
		line+=")";
		dispatch(n.getNode(1));
		
	}
	public void visitBasicCastExpression(GNode n){ 
		line+="(";
	 	dispatch(n.getNode(0));
	 	line+=")";
	 	dispatch(n.getNode(2)); 
	}
	
	public void visitUnaryExpression(GNode n){
		visit(n);
	}
	
	public void visitMultiplicativeExpression(GNode n){
		visit(n);
	}
	public void visitSubscriptExpression(GNode n){
		line+="(*";
		dispatch(n.getNode(0));
		line+=")[";
		dispatch(n.getNode(1));
		line+="]";
		
	}
	public void visitIntegerLiteral(GNode n){
		line+=(String) n.get(0);
	}
	public void visitFloatingPointLiteral(GNode n){
		line+=(String) n.get(0);
	}
	public void visitStringLiteral(GNode n){
		if (assignmentFlag){
			line+=" new __String(" + n.get(0) +")";
		}
		else{
			line+=(String) n.get(0);
		}
	}
	public void visitBooleanLiteral(GNode n){
		line+=(String) n.get(0);		
	}
	
	public void visitBlock(GNode n){
//		line+="{\n";
		ccBlock blockStatement = new ccBlock(n, block.variables, block.localVariables, block.classList, block.currentClass, false);
//		line+="}\n";
		while (!blockStatement.blockLines.isEmpty())
			line += blockStatement.blockLines.remove()+"\n";
	}
	
	public void visitConditionalStatement(GNode n){
		line += "if("+new ccStatement((GNode)n.get(0), block).publish()+")";
		dispatch(n.getNode(1));
		if(n.get(2)!=null){
			line += "else ";
			dispatch(n.getNode(2));}
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
		line = "while("+new ccStatement((GNode)n.get(0), block).publish()+")";
	}
	public void visitDeclarator(GNode n){
		line+=n.getString(0);
		if(n.getNode(1)==null)
			line+="= ";
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
		if(!(n.isEmpty()) && !(n.get(0) instanceof String) && !n.getNode(0).getName().equals("AdditiveExpression"))	line += "(";
		int i = 0;
		for (Object o : n){
			if(i > 0){line += ", ";}
			if (o instanceof Node){
				dispatch((Node)o);
			}
			i++;
		}
		if(!(n.isEmpty()) && !(n.get(0) instanceof String) && !n.getNode(0).getName().equals("AdditiveExpression"))	line += ")";
	}
	
	public void visitSelectionExpression(GNode n){
		if(block.variables.get(n.getString(n.size()-1)) == null){
//			ccClass c;
//			String oType = block.variables.get(n.getNode(0).getString(0)).getType();
//			for(int i = 3; i<block.classList.size(); i++){
//				if(block.classList.get(i).getName().contentEquals(oType)){
//					c = block.classList.get(i);
//					line += c.get_Name() + c.findField(n.getString(n.size()-1)).publish();
//				}
//			}
			line += n.getNode(0).getString(0) + "->" + n.getString(n.size()-1);
		}
		else{
			line+= block.variables.get((n.getString(n.size()-1))).publish();
		}
		
	}
	public void visitThisExpression(GNode n){
		line+="__this";
	}
	public void visitNewArrayExpression(GNode n){
		line+="new __rt::Array<";
		dispatch(n.getNode(0));
		line+=">(";
		dispatch(n.getNode(1));
		line+=")";
	}
	public void visitPrimaryIdentifier(GNode n){
		if(!block.variables.isEmpty()&&block.variables.get(n.get(0))!=null&&!block.getIsConstructorBlock()){
			line+= block.variables.get(n.get(0)).publish();
		}
		else{
			if(!((block==null)||(block.getLocalVariables().containsKey(n.get(0)))||(block.getIsConstructorBlock()))){
				line+= "__this->" + ccHelper.convertType(n.getString(0));	
			}	
			else{
				line+= ccHelper.convertType(n.getString(0));
			}
		}
	}
	
	public void visitNullLiteral(GNode n){
		line += "__rt::null()";
	}
	
	public void visitNewClassExpression(GNode n){
		line+= "new __";
		visit(n);
	}
	
	public  String publish() {
		return line;
	}
	
	public String[] findArgumentTypes(Node n){
		String[] argTypes = new String[n.size()];
		for(int i=0; i< n.size(); i++){
			if(n.getNode(i).getName().matches("IntegerLiteral")){
				argTypes[i] = "int32_t";
			}
			else if(n.getNode(i).getName().matches("FloatingPointLiteral")){
				argTypes[i] = "double";
			}
			else if(n.getNode(i).getName().matches("StringLiteral")){
				argTypes[i] = "String";
			}
			else if(n.getNode(i).getName().matches("BooleanLiteral")){
				argTypes[i] = "bool";
			}
			else if(n.getNode(i).getName().matches("ThisExpression")){
				argTypes[i] = block.currentClass;
			}
			else if(n.getNode(i).getName().matches("PrimaryIdentifier")){
				argTypes[i] = block.variables.get(n.getNode(i).getString(0)).getType();
			}
			else if(n.getNode(i).getName().matches("AdditiveExpression")){
				argTypes[i] = "int32_t";
			}
			else if(n.getNode(i).getName().matches("CastExpression")){
				argTypes[i] = n.getNode(i).getNode(0).getNode(0).getString(0);
			}
		}
		return argTypes;
	}
	
	public void visitAdditiveExpression(GNode n){
		if(n.toString().contains("StringLiteral")){
			line+= "({";
			line+= "String tempResult=new __String(\"\");\n";
			buildCluster(n);
			line+="})";
			}
		else
			visit(n);
	}
	
	 private void buildCluster(GNode n) {
		boolean notDeclared = true;
		String part2 = null;
		for(Object q:n){
			if(q.toString().startsWith("AdditiveExpression"))
				buildCluster((GNode)q);
			else if(q.toString().startsWith("StringLiteral")){
				GNode b = (GNode)q;
				line+="String temp"+clusterCount+" = new __String("+b.getString(0)+"); \n";
				part2 = "temp"+(clusterCount);
				clusterCount++;
			}
			else if(q.toString().startsWith("PrimaryIdentifier")){
				GNode b = (GNode)q;
////				line+="String temp"+clusterCount+" = "+b.getString(0)+"+\"\"; \n";
////				part2 = "temp"+(clusterCount);
//				System.out.println(b.getString(0));
				part2 = b.getString(0);
				clusterCount++;
			}
			else if(q.toString().startsWith("IntegerLiteral")){
				GNode b = (GNode)q;
				line+="int temp"+clusterCount+" = "+b.getString(0)+"; \n";
				part2 = "temp"+(clusterCount);
				clusterCount++;
			}
			else if(q.toString().startsWith("CharacterLiteral")){
				GNode b = (GNode)q;
				line+="char temp"+clusterCount+" = "+b.getString(0)+"; \n";
				part2 = "temp"+(clusterCount);
				clusterCount++;
			}
			else if(q.toString().startsWith("BasicCastExpression")){
				line+="tempResult=tempResult+";
				dispatch((GNode)q);
				line+="; \n";
				notDeclared=false;
			}
			if(clusterCount==2&&notDeclared){
				line+="tempResult=tempResult+"+part2+"; \n";
				notDeclared=false;
			}
		}
		line+="tempResult=tempResult+"+part2+";\n";
	}
	
	public void visitLogicalAndExpression(GNode n){
	    	if(n.getGeneric(0).toString().contains("LogicalAndExpression")){
	            dispatch(n.getGeneric(0));
	        }
	        else{
	            line+="(";        
	            dispatch(n.getGeneric(0));
	            line+=")";
	            }
	        line+=" && ";
	        line+="(";        
	        dispatch(n.getGeneric(1));
	        line+=")";

 }
	 public void visitEqualityExpression(GNode n){ 
		 visit(n);
	}
	public void visitLogicalNegationExpression(GNode n){
		line+="!(";
		visit(n);
		line+=")";
	}
	public void visitInstanceOfExpression(GNode n){
		String compareObject = n.getNode(1).getNode(0).getString(0);
		String compareVariable = n.getNode(0).getString(0);
		
		line+= "({" + compareObject + " " + "__instCheck = new __" + compareObject;
		if(compareObject.equals("String"))
			line+="(\"\"); ";
		else
			line+="(); ";
		line+= "__instCheck->__vptr->getClass(__instCheck)->__vptr->isInstance(__instCheck->__vptr->getClass(__instCheck), "+compareVariable+");})";
	}
	
	public void visit(Node n) {
		for (Object o : n){
			if (o instanceof Node){
				dispatch((Node)o);
			}
			else if(o instanceof String){
				if(((String) o).matches("=")){
					line+=" = ";
				}
				else{
					line+=ccHelper.convertType(o.toString());
				}
			}
		}
	}
}
