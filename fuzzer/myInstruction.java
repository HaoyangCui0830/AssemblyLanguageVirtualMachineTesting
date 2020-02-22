
public class myInstruction {
	public final static String[] Operations = {"add","sub","mul","div","ret","mov","ldr","str","jmp","jz"};
	public final static String[] valid_regs = {"r0","r1","r2","r3","r4","r5","r6","r7","r8","r9",
										"r10","r11","r12","r13","r14","r15","r16","r17","r18","r19",
										"r20","r21","r22","r23","r24","r25","r26","r27","r28","r29",
										"r30","r31"};
	public final static String[] invalid_regs = {"rr","r32","r","a1"};
	
	public final static String[] valid_input = {"mov","add","sub","mul","ldr","str","jz","jmp","div","ret"};
	
	public static String vrRandomSelect() {
		double d = Math.random();
		int i = (int)(Math.random()*32);
		return valid_regs[i];
	}
	public static String invrRandomSelect() {
		double d = Math.random();
		int i = (int)(d*invalid_regs.length)/invalid_regs.length;
		return invalid_regs[i];
	}
	public static String invrRandomSelect(int i) {
		return invalid_regs[i];
	}
	
	
	public static String valid_add() {
		return "add "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect(); 
	}
	public static String invalid_add_regs(int i) {
		return "add "+ vrRandomSelect() + " " + vrRandomSelect() + " " + invrRandomSelect(i); 
	}
	public static String invalid_add_redund() {
		return "add "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect();
	}
	public static String invalid_add_lack() {
		return "add "+ vrRandomSelect() + " " + vrRandomSelect();
	}
	
	
	public static String valid_sub() {
		return "sub "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect(); 
	}
	public static String invalid_sub_regs(int i) {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect() + " " + invrRandomSelect(i); 
	}
	public static String invalid_sub_redund() {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect();
	}
	public static String invalid_sub_lack() {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect();
	}
	
	public static String valid_mul() {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect(); 
	}
	public static String invalid_mul_regs(int i) {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect() + " " + invrRandomSelect(i); 
	}
	public static String invalid_mul_redund() {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect();
	}
	public static String invalid_mul_lack() {
		return "mul "+ vrRandomSelect() + " " + vrRandomSelect();
	}
	
	public static String valid_div() {
		return "div "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect(); 
	}
	public static String invalid_div_regs(int i) {
		return "div "+ vrRandomSelect() + " " + vrRandomSelect() + " " + invrRandomSelect(i); 
	}
	public static String invalid_div_redund() {
		return "div "+ vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect() + " " + vrRandomSelect();
	}
	public static String invalid_div_lack() {
		return "div "+ vrRandomSelect() + " " + vrRandomSelect();
	}
	
	public static String valid_ret() {
		return "ret " + vrRandomSelect(); 
	}
	public static String invalid_ret_regs(int i) {
		return "ret "+ invrRandomSelect(i); 
	}
	public static String invalid_ret_redund() {
		return "ret "+ vrRandomSelect() + " " + vrRandomSelect();
	}
	public static String invalid_ret_lack() {
		return "ret ";
	}
	
	public static String valid_mov(int i) {
		return "mov " + vrRandomSelect() +" "+ Integer.toString(i);
	}
	public static String invalid_mov_regs(int i) {
		return "mov " + invrRandomSelect(i) +" "+ Integer.toString(i);
	}
	public static String set_value(String regs, int i) {
		return "mov " + regs + " " + Integer.toString(i);
	}
	
	public static String valid_str(String regs, int i, String regs1) {
		return "str " + regs + " " + Integer.toString(i) + " " + regs1;
	}
	
	public static String valid_ldr(String regs, String regs1, int i) {
		return "ldr " + regs + " " + regs1 + " " + Integer.toString(i);
	}
	
	public static String valid_jmp(int i) {
		return "jmp " + Integer.toString(i);
	}
	
	public static String valid_jz(String regs, int i) {
		return "jz " + regs + " " + Integer.toString(i);
	}
	
	
}
