package oop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashSet;

public class JavaGrinder {
	
	public class RequiredFileNotFoundException extends Exception{
		public String fileName;
		public RequiredFileNotFoundException(String filename){
			fileName = filename;
		}
	}
	
	public JavaGrinder(String args[]) throws RequiredFileNotFoundException, IOException{
		
		String dirName = args[0].substring(0, args[0].indexOf('.')) + "_Output";
		File directory = new File(dirName);
		directory.mkdir();
		
		File check = new File("ptr.cc");
		File copy;
		if(!check.exists()){
			throw new RequiredFileNotFoundException(check.getName());
		}
		copy = new File(dirName + "/ptr.cc");
		copyFile(check, copy);
		check = new File("ptr.h");
		if(!check.exists()){
			throw new RequiredFileNotFoundException(check.getName());
		}
		copy = new File(dirName + "/ptr.h");
		copyFile(check, copy);
		check = new File("java_lang.cc");
		if(!check.exists()){
			throw new RequiredFileNotFoundException(check.getName());
		}
		copy = new File(dirName + "/java_lang.cc");
		copyFile(check, copy);
		check = new File("java_lang.h");
		if(!check.exists()){
			throw new RequiredFileNotFoundException(check.getName());
		}
		copy = new File(dirName + "/java_lang.h");
		copyFile(check, copy);
		/* End of file Tests */
		
		DependencyMaster dependency = new DependencyMaster();
		dependency.checkDependencies(args);
		dependency.checkForFiles(args);
		
		HashSet<String> translateMe = new HashSet<String>();
		for(int i=0; i< args.length; i++){
			translateMe.add(args[i]);
		}
		hMaster hTranslate = new hMaster(translateMe, directory);
		HashSet<String> overloads = hTranslate.overloads;
		ccMaster ccTranslate = new ccMaster(translateMe, overloads, directory);
		
		System.out.println("Checkit: done.");
	}
	
	/* Sun wrote this method, not me. */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;

	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	        destination.transferFrom(source, 0, source.size());
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	public static void main(String args[]){
		try{
			JavaGrinder jg = new JavaGrinder(args);
		} catch(RequiredFileNotFoundException e){
			System.out.println(e.fileName + " not found. It's required.");
		} catch(IOException e){
			System.out.println("While copying pre-made files: " + e);
		}
	}
}

