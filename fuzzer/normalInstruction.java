import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class normalInstruction implements iFuzzerTest{
	private static String OUTPUT_FILE = "";
	public int type = 0;
	public normalInstruction(String file) {
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
            

            for(int i=0;i<myInstruction.valid_input.length;i++) {
            	pw.println(getInstructions(myInstruction.valid_input[i],type)+";");
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
	
	public static String getInstructions(String in, int type) {
		if(in.equals("add")) {
			return myInstruction.valid_add();
		}
		else if(in.equals("sub")) {
			return myInstruction.valid_sub();
		}
		else if(in.equals("mul")) {
			return myInstruction.valid_mul();
		}
		else if(in.equals("div")) {
			return myInstruction.valid_div();
		}
		else if(in.equals("ret")) {
			return myInstruction.valid_ret();
		}
		else if(in.equals("str")) {
			return myInstruction.valid_str(myInstruction.vrRandomSelect(), 1, myInstruction.vrRandomSelect());
		}
		else if(in.equals("ldr")) {
			return myInstruction.valid_ldr(myInstruction.vrRandomSelect(), myInstruction.vrRandomSelect(), 1);
		}
		else if(in.equals("mov")) {
			return myInstruction.valid_mov(1);
		}
		else if(in.equals("jmp")) {
			return myInstruction.valid_jmp(1);
		}
		else if(in.equals("jz")) {
			return myInstruction.valid_jz(myInstruction.vrRandomSelect(), 1);
		}else {
			return " ";
		}
	}
}