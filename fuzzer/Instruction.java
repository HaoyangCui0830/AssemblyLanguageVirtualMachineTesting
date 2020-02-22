import java.util.Arrays;
import java.util.ArrayList;

public enum Instruction {
    ADD("add",new OperandType[]{OperandType.REGISTER,OperandType.REGISTER,OperandType.REGISTER}),
    SUBTRACT("sub",new OperandType[]{OperandType.REGISTER,OperandType.REGISTER,OperandType.REGISTER}),
    MULT("mul",new OperandType[]{OperandType.REGISTER,OperandType.REGISTER,OperandType.REGISTER}),
    DIVIDE("div",new OperandType[]{OperandType.REGISTER,OperandType.REGISTER,OperandType.REGISTER}),
    RETURN("ret",new OperandType[]{OperandType.REGISTER}),
    LOAD("ldr",new OperandType[]{OperandType.REGISTER,OperandType.REGISTER,OperandType.VALUE}),
    STORE("str",new OperandType[]{OperandType.REGISTER,OperandType.VALUE,OperandType.REGISTER}),
    MOVE("mov",new OperandType[]{OperandType.REGISTER,OperandType.VALUE}),
    JUMP("jmp",new OperandType[]{OperandType.VALUE}),
    JZ("jz",new OperandType[]{OperandType.REGISTER,OperandType.VALUE});

    public static String getBNF(){
        String grammar = "<INSTRUCTION> ::= \n";
        Instruction[] INSTS = Instruction.values();
        boolean firstInst = true;
        for (Instruction inst : INSTS){
            if (firstInst){
                grammar += "      \"";
                firstInst = false;
            }else{
                grammar += "    | \"";
            }
            grammar += inst.getOpcode() + "\"";
            for (OperandType op : inst.getOperands()){
                grammar += " <" + op.toString() + ">";
            }
            grammar += "\n";
        }
        return grammar;
    }
    
    private final String opcode;
    private final OperandType[] operands;

    Instruction(String opcode, OperandType[] operands){
        this.opcode = opcode;
        this.operands = operands;
    }

    public String getOpcode(){
        return opcode;
    }
    
    public OperandType[] getOperands(){
        return operands;
    }

    public String toString(){
        String operandsString = "";
        for (OperandType op : operands) {
            operandsString += " " + op.toString();
        }
        return "\"" + opcode + "\"" + operandsString;
    }
    
}
