import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class OverRegisterFuzzerTest implements iFuzzerTest {
	private static final String OUTPUT_FILE = "fuzz.s";
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
            pw.println("MOV R0 1;");
			pw.println("MOV R1 1;");
			pw.println("STR R2 1;");
			pw.println("RET R32;");
            
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
