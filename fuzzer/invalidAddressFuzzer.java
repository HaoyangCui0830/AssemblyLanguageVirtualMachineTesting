import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class invalidAddressFuzzer implements iFuzzerTest{
	private static String OUTPUT_FILE = "";
	public static boolean str_pre = false;
	public static boolean ldr_pre = false;
	public int type;
	public static String register = "";
	public invalidAddressFuzzer(int input_type, String file) {
		OUTPUT_FILE = file;
		this.type = input_type;
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
            	pw.println(getInstructions(myInstruction.valid_input[i])+";");
            	if(str_pre && this.type == 0) {
        			str_pre = false;
        			pw.println(myInstruction.valid_str(register, 1, myInstruction.vrRandomSelect()) + ";");
        		}
            	else if(ldr_pre && this.type == 1) {
        			ldr_pre = false;
        			pw.println(myInstruction.valid_ldr(myInstruction.vrRandomSelect(),register, 1)+";");
        		}
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
	
	public static String getInstructions(String in) {
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
			register = myInstruction.vrRandomSelect();
			str_pre = true;
			return "mov " + register + " " + "65535";
			//return myInstruction.valid_str(myInstruction.vrRandomSelect(), 1, myInstruction.vrRandomSelect());
		}
		else if(in.equals("ldr")) {
			register = myInstruction.vrRandomSelect();
			ldr_pre = true;
			return "mov " + register + " " + "65535";
			//return myInstruction.valid_ldr(myInstruction.vrRandomSelect(), myInstruction.vrRandomSelect(), 1);
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