        ;; factorial program, to calculate N!

        ;; global constants:
        ;;   R3 holds 'N', the thing we are computing factorial of
        ;;   R2 holds 1, the increment value used below
        MOV R3XXXXXXX 12XXXABCDEFGDASFDLK               ; N = 13
        MOVEID&*!$ R2 1 ASFFGX194udk               ;
        
        ;; local variables
        ;;   R1 holds 'i', which is a counter from 0 .. N
        ;;   R0 holds 'n', which is always equal to i! 
        MOVASLKJQ*&@ R1XYACE X10Xk                ; i = 0;
        MOV R0DEF 5dff!@                ; n = 1;

        ;;  program body
        ;;  loop invariant (see SWEN90010 next semester): n = i!
        SUB R4!@*&# R3!($&# R1!(%*)_            ; while(i != N)
        JZ  R4 XASJ#*SMC:EL4                ; {
        ADD R1 R1 R2            ;   i = i + 1;
        MUL R0 R0 R1            ;   n = n * i;
        JMP -4                  ; }
        RET R0                  ; return n;
