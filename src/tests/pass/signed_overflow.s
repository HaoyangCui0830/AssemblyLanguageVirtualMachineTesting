	;; this program demonstrates how the machine implements
	;; 'wraparound-on-signed-overflow' semantics
	;;  the idea is that DIE_HORRIBLY, if executed, would cause
	;;  the machine to crash but that it won't get executed by this
	;;  program if the machine is working how we think it does
	MOV R0 65535
	MOV R1 1
	ADD R0 R0 R1            ; R0 = 65536
	MUL R0 R0 R0            ; signed overflow, R0 = 0
	JZ  R0 2
	DIE_HORRIBLY
	;;  this next part doesn't have much to do with signed overflow
	;;  instead it shows how we can index memory:
	;;  we do a load at memory address 0 but from a register
	;;  whose value is -65535 with offset 65535
	MOV R0 -65535
	MOV R1 0
	MOV R5 42
	STR R1 0 R5             ; mem[0] = 42
	LDR R2 R0 65535         ; r2 = mem[0] (no overflow)
	SUB R2 R2 R5
	JZ  R2 2
	DIE_HORRIBLY
	MOV R0 0
	RET R0
	