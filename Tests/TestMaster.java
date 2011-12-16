package oop.JavaGrinder.Tests;

import java.io.File;
import java.io.IOException;

import oop.JavaGrinder;
import oop.JavaGrinder.RequiredFileNotFoundException;



public class TestMaster {
	
	String[] testArray;
	
	public TestMaster() {
		String currentDir = "/home/user/xtc/src/Translator/src/oop/Tests/";
		testArray = new String[20];
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
		testArray[14] = currentDir + "CastTest.java";
		testArray[15] = currentDir + "OverloadTest.java";
		testArray[16] = currentDir + "NestedMethodTest.java";
		testArray[17] = currentDir + "MultipleClassTest.java";
		testArray[18] = currentDir + "ArrayTest.java";
		testArray[19] = currentDir + "LoopTest.java";

		try{
			JavaGrinder jg = new JavaGrinder(testArray);
		} catch (RequiredFileNotFoundException e){
			System.out.println(e.fileName + " not found. It's required.");
		} catch(IOException e){
			System.out.println("While copying pre-made files: " + e);
		}
	}
	
	
	
	public static void main(String[] args){
		new TestMaster();
	}

}
