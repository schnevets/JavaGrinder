package Trans;

public class DependencyTester {

	public static void main(String[] args) {
		System.out.println((new DependencyMaster()).getDependencies("/home/user/xtc/src/Translator/src/Trans/Testfile.java").toString());
	}

	
}
