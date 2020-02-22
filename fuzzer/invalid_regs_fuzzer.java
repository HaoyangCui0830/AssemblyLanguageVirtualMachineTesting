import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class invalid_regs_fuzzer implements iFuzzerTest{
	private static String OUTPUT_FILE = "";
	public int type = 0;
	public invalid_regs_fuzzer(int type,String file) {
		this.type = type;
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
			return myInstruction.invalid_add_regs(type);
		}
		else if(in.equals("sub")) {
			return myInstruction.invalid_sub_regs(type);
		}
		else if(in.equals("mul")) {
			return myInstruction.invalid_mul_regs(type);
		}
		else if(in.equals("div")) {
			return myInstruction.invalid_div_regs(type);
		}
		else if(in.equals("ret")) {
			return myInstruction.invalid_ret_regs(type);
		}
		else if(in.equals("str")) {
			return myInstruction.valid_str(myInstruction.vrRandomSelect(), 1, myInstruction.vrRandomSelect());
		}
		else if(in.equals("ldr")) {
			return myInstruction.valid_ldr(myInstruction.vrRandomSelect(), myInstruction.vrRandomSelect(), 1);
		}
		else if(in.equals("mov")) {
			return myInstruction.invalid_mov_regs(type);
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
