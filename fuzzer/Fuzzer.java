import java.io.IOException;
import java.io.PrintStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;


/* a stub for your team's fuzzer */
public class Fuzzer {

    private static final String OUTPUT_FILE = "fuzz.s";
    private static final String fileName="Count.txt";
    public static void main(String[] args) throws IOException {
        FileOutputStream out = null;
        PrintWriter pw = null;
        try {
        	String str =readString();
        	out = new FileOutputStream(OUTPUT_FILE);
            pw = new PrintWriter(out);
        	
        	
        	iFuzzerTest[] testCases = {
        			new OverLineLengthFuzzerTest(OUTPUT_FILE),
        			new normalInstruction(OUTPUT_FILE),
        			new invalid_regs_fuzzer(0,OUTPUT_FILE),
        			new invalid_regs_fuzzer(1,OUTPUT_FILE),
        			new invalid_regs_fuzzer(2,OUTPUT_FILE),
        			new invalid_regs_fuzzer(3,OUTPUT_FILE),
        			new redundInstructionFuzzer(OUTPUT_FILE),
        			new redundInstructionFuzzer(OUTPUT_FILE),
        			new lackInstructionFuzzer(OUTPUT_FILE),
        			new lackInstructionFuzzer(OUTPUT_FILE),
        			new invalidAddressFuzzer(0, OUTPUT_FILE),
        			new invalidAddressFuzzer(1, OUTPUT_FILE),
        			new NoRecogniseFuzzer(OUTPUT_FILE),
        			new NegPCFuzzer(OUTPUT_FILE),
        			new DivZeroFuzzer(OUTPUT_FILE),
        			new HighCoverageFuzzer(OUTPUT_FILE),
        			new OverProgramLengthFuzzerTest(OUTPUT_FILE),
        			new MutationBasedFuzzer(OUTPUT_FILE),
        			new RandomFuzzer(OUTPUT_FILE)};

            
        	

            Integer i = Integer.parseInt(str);
            if(i<testCases.length) {
            	System.out.println(i);
            	testCases[i].createTestCase();
            	i++;
            	String wstr = Integer.toString(i);
            	WriteStringToFile(wstr);
            }
            else if(i==testCases.length) {
            	WriteStringToFile("0");
            }
        }catch (Exception e){
            e.printStackTrace(System.err);
            System.exit(1);
        }finally{
            if (pw != null){
                pw.flush();
            }
            if (out != null){
                out.close();
            }
        }

    }
    
    private static String readString(){
    	StringBuffer str=new StringBuffer("");
    	File file=new File(fileName);
    	try {             
    		FileReader fr=new FileReader(file);
    		int ch = 0;
    		while((ch = fr.read())!=-1 )
    		{                 
    			//System.out.print((char)ch);
    			str.append((char)ch);
    		}             
    		fr.close();
    			
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    		System.out.println("error");
    		}         
    	return str.toString();     
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
