import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
public class NegPCFuzzer implements iFuzzerTest{

	private static String OUTPUT_FILE = "";
	
	public NegPCFuzzer(String file) {
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

            pw.println("JMP -3;");
            pw.println("RET R1;");

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
