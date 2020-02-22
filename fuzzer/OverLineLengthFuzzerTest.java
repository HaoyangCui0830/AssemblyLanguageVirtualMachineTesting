import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class OverLineLengthFuzzerTest implements iFuzzerTest{
	private static String OUTPUT_FILE = "";
	public OverLineLengthFuzzerTest(String file) {
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
            for(int i = 0;i<1025;i++) {
            	pw.print(" ");
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

}
