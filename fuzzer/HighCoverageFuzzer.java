import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HighCoverageFuzzer implements iFuzzerTest {
	private static String OUTPUT_FILE = "fuzz.s";
    public HighCoverageFuzzer(String file) {
        OUTPUT_FILE = file;
    }
	@Override
	public void createTestCase() throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream out = null;
        PrintWriter pw = null;
        try {
            out = new FileOutputStream(OUTPUT_FILE);
            pw = new PrintWriter(out);
            
            /* We just print one instruction.
               Hint: you might want to make use of the instruction
               grammar which is effectively encoded in Instruction.java */
            for(int i = 0;i<65515;i++){
            	pw.println("");
            }
            pw.println("   ;;;"); 
            pw.println("MOV R0 1;");  
            pw.println("MOV R1 1;");  
            pw.println("MOV R2 1;");  
            pw.println("ADD R0 R1 R2;");  
            pw.println("SUB R0 R1 R2;");  
            pw.println("MUL R0 R1 R2;");  
            pw.println("DIV R0 R1 R2;");  
            pw.println("MOV R0 1;");  
            pw.println("MOV R1 -1;");  
            pw.println("LDR R0 R0 65535;");  
            pw.println("LDR R0 R1 0;");  
            pw.println("STR R0 65535 R0;");  
            pw.println("STR R1 0 R0;");  
            pw.println("JMP 1;");  
            pw.println("JZ R5 1;");  
            pw.println("JZ R1 1;");
            pw.println("MOV R0 1;");  
            pw.println("MOV R1 1;");  
            pw.println("STR R2 1 R1;;");  
            pw.println("LDR R1 R2 1;"); 
            for(int i = 0;i<1024;i++) {
            	pw.print(" ");
            }
            pw.println("\0");
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

}
