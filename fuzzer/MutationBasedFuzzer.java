import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MutationBasedFuzzer implements iFuzzerTest {
    private static String OUTPUT_FILE = "fuzz.s";
    public MutationBasedFuzzer(String file) {
        OUTPUT_FILE = file;
    }
    @Override
    public void createTestCase() throws IOException {
        // TODO Auto-generated method stub
        FileOutputStream out = null;
        PrintWriter pw = null;
        Random r = new Random();
        String add = getInstructions("add");
        StringBuilder addBuilder = new StringBuilder(add);
        addBuilder.setCharAt(r.nextInt(add.length()),(char)(r.nextInt(100)));
        String sub = getInstructions("sub");
        StringBuilder subBuilder = new StringBuilder(sub);
        subBuilder.setCharAt(r.nextInt(sub.length()),(char)(r.nextInt(100)));
        String mul = getInstructions("mul");
        StringBuilder mulBuilder = new StringBuilder(mul);
        mulBuilder.setCharAt(r.nextInt(mul.length()),(char)(r.nextInt(100)));
        String div = getInstructions("div");
        StringBuilder divBuilder = new StringBuilder(div);
        divBuilder.setCharAt(r.nextInt(div.length()),(char)(r.nextInt(100)));
        String str = getInstructions("str");
        StringBuilder strBuilder = new StringBuilder(str);
        strBuilder.setCharAt(r.nextInt(str.length()),(char)(r.nextInt(100)));
        String ldr = getInstructions("ldr");
        StringBuilder ldrBuilder = new StringBuilder(ldr);
        ldrBuilder.setCharAt(r.nextInt(ldr.length()),(char)(r.nextInt(100)));
        String mov = getInstructions("mov");
        StringBuilder movBuilder = new StringBuilder(mov);
        movBuilder.setCharAt(r.nextInt(mov.length()),(char)(r.nextInt(100)));
        String jmp = getInstructions("jmp");
        StringBuilder jmpBuilder = new StringBuilder(jmp);
        jmpBuilder.setCharAt(r.nextInt(jmp.length()),(char)(r.nextInt(100)));
        String jz = getInstructions("jz");
        StringBuilder jzBuilder = new StringBuilder(jz);
        jzBuilder.setCharAt(r.nextInt(jz.length()),(char)(r.nextInt(100)));
        String ret = getInstructions("ret");
        StringBuilder retBuilder = new StringBuilder(ret);
        retBuilder.setCharAt(r.nextInt(ret.length()),(char)(r.nextInt(100)));

        try {
            out = new FileOutputStream(OUTPUT_FILE);
            pw = new PrintWriter(out);
            pw.println(addBuilder.toString());
            pw.println(subBuilder.toString());
            pw.println(mulBuilder.toString());
            pw.println(divBuilder.toString());
            pw.println(strBuilder.toString());
            pw.println(ldrBuilder.toString());
            pw.println(movBuilder.toString());
            pw.println(jmpBuilder.toString());
            pw.println(jzBuilder.toString());
            pw.println(retBuilder.toString());
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