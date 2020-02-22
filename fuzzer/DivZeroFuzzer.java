import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class DivZeroFuzzer implements iFuzzerTest {
    private static String OUTPUT_FILE = "";
    public DivZeroFuzzer(String file) {
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

            pw.println("MOV R1 0;");
            pw.println("MOV R2 0;");
            pw.println("DIV R3 R1 R2;");
            pw.println("RET R3;");

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        } finally {
            if (pw != null) {
                pw.flush();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}