package oop;

import java.io.File;
import java.io.IOException;

//import oop.JavaGrinder.RequiredFileNotFoundException;



public class TestMaster {
	
	String[] testArray;
	
	public TestMaster() {
		String currentDir = "";
		testArray = new String[21];
		testArray[0] = currentDir + "CompileTest.java";
		testArray[1] = currentDir + "ConstructorTest.java";
		testArray[2] = currentDir + "MethodDecTest.java";
		testArray[3] = currentDir + "MethodModifierTest.java";
		testArray[4] = currentDir + "ParameterReturnTest.java";
		testArray[5] = currentDir + "SystemOutTest.java";
		testArray[6] = currentDir + "VariableTest.java";
		testArray[7] = currentDir + "PrimitiveTest.java";
		testArray[8] = currentDir + "ConditionalTest.java";
		testArray[9] = currentDir + "ConcatTest.java";
		testArray[10] = currentDir + "ExceptionTest.java";
		testArray[11] = currentDir + "ExtensionTest.java";
		testArray[12] = currentDir + "OverrideTest.java";
		testArray[13] = currentDir + "DependencyTest.java";
		testArray[14] = currentDir + "GenericTest.java";
		testArray[15] = currentDir + "CastTest.java";
		testArray[16] = currentDir + "OverloadTest.java";
		testArray[17] = currentDir + "NestedMethodTest.java";
		testArray[18] = currentDir + "MultipleClassTest.java";
		testArray[19] = currentDir + "NestedClassTest.java";
		testArray[20] = currentDir + "ArrayTest.java";

		try{
			JavaGrinder jg = new JavaGrinder(testArray);
		} //catch (RequiredFileNotFoundException e){
			//System.out.println(e.fileName + " not found. It's required.");
		//} 
		catch(Exception e){
			System.out.println("While copying pre-made files: " + e);
		}
	}
	
	
	
	public static void main(String[] args){
		new TestMaster();
	}

}
