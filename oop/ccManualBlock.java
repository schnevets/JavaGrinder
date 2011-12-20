package oop;

import java.util.LinkedList;

public class ccManualBlock extends ccBlock {
	
	public LinkedList<String> blockLines;
	
	public ccManualBlock() {
		blockLines = new LinkedList<String>();
	}
	
	public LinkedList<String> publish() {
		return blockLines;
	}
	
	public void addCustomLine(String s){
		blockLines.add(s + "\n");
	}
	
}
