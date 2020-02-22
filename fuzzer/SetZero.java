import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class SetZero {
	private static final String fileName="Count.txt";
	public static void main(String[] args) throws IOException{
		WriteStringToFile("0");
	}
	
	public static void WriteStringToFile(String writeStr) {
    	try {
    		File file = new File(fileName);
    		PrintStream ps = new PrintStream(new FileOutputStream(file));
    		ps.print(writeStr); 
    	} catch (FileNotFoundException e) {
    		// TODO Auto-generated catch block            
    		e.printStackTrace();        
    	}    
    }
	
}
