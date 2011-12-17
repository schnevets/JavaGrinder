package oop.JavaGrinder.Tests;

public class LoopTest {
	
	public void forMethod(){
		int max = 10;
		for(int i=0;i<max;i++){
			System.out.println(i);
		}
		int i = 10;
		for(;i>0;i--){
			System.out.println(i);
		}
	}
	
	public void whileMethod(){
		int k = 10;
		while(k>0){
			System.out.println(k);
			k--;
		}
		do{
			System.out.println("A tall lad");
			++k;
		}
		while(k<10);

		
	}
	
	public static void main(){
		LoopTest l =new LoopTest();
		l.forMethod();
		l.whileMethod();
	}

}
