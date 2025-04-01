        MOV A,#2
        PUSH A
        MOV A,#1
        POP B
        ADD A,B
        MOV A,#3
        PUSH A
        MOV A,#2
        PUSH A
        MOV A,#1
        POP B
        ADD A,B
        POP B
        ADD A,B
        MOV A,#3
        PUSH A
        MOV A,#2
        POP B
        SUB A,B
    DD x
        MOV A,#7
        MOV [x],A
    DD y
    MOV A,#69
    MOV [y],A
        MOV A,[x]
        PUSH A
        PUSH A
        MOV A,#5
        POP B
        SUB A,B
        MOV A,#3
        PUSH A
        MOV A,[x]
        PUSH A
        POP B
        MUL A,B
        PUSH A
        MOV A,#2
        PUSH A
        MOV A,#1
        POP B
        SUB A,B
        POP B
        ADD A,B
        MOV A,#6
        PUSH A
        MOV A,[x]
        PUSH A
        POP B
        MUL A,B
        PUSH A
        MOV A,#5
        PUSH A
        MOV A,#4
        POP B
        SUB A,B
        POP B
        ADD A,B
    MOV A,#1
        JE label_else_1
            MOV A,#4
            PUSH A
            MOV A,#3
            POP B
            ADD A,B
        JMP label_if_end_1
    label_else_1:
    label_if_end_1:
    MOV A,#0
        JE label_else_2
            MOV A,#5
        JMP label_if_end_2
    label_else_2:
            MOV A,#6
    label_if_end_2:
    MOV A,#1
    PUSH A
    MOV A,#1
    POP B
    CMP A,B
    JE label_comp_1
    MOV A,#1
    JMP label_comp_end_1
    label_comp_1:
    MOV A,#0
    label_comp_end_1:
        JE label_else_3
            MOV A,#6
        JMP label_if_end_3
    label_else_3:
            MOV A,#9
    label_if_end_3:
    MOV A,#1
    PUSH A
    MOV A,#0
    POP B
    CMP A,B
    JG label_comp_2
    MOV A,#1
    JMP label_comp_end_2
    label_comp_2:
    MOV A,#0
    label_comp_end_2:
        JE label_else_4
            MOV A,#6
        JMP label_if_end_4
    label_else_4:
            MOV A,#7
    label_if_end_4:
    square:
    PUSH BP
    MOV BP, SP

    PUSH A
    PUSH B

                MOV A,#2
                PUSH A
                MOV A,#1
                POP B
                SUB A,B
    POP BP
    RET
            MOV A,#1
            PUSH A
            MOV A,#11
            POP B
            ADD A,B
            PUSH A
            MOV A,#2
            PUSH A
            MOV A,#3
            PUSH A
            CALL square
            POP B
            POP B
            POP B