        PUSH #1
        PUSH #2
        ADD
        PUSH #1
        PUSH #2
        ADD
        PUSH #3
        ADD
        PUSH #2
        PUSH #3
        SUB
    DD x
        PUSH #7
        POP [x]
    DD y
    PUSH #69
    POP [y]
        PUSH #5
        PUSH [x]
        SUB
        PUSH #1
        PUSH #2
        SUB
        PUSH [x]
        PUSH #3
        MUL
        ADD
        PUSH #4
        PUSH #5
        SUB
        PUSH [x]
        PUSH #6
        MUL
        ADD
    Terminal node:<EOF>