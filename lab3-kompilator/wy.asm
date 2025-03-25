        PUSH #1
        PUSH #2
        ADD
        PUSH #1
        PUSH #2
        ADD
        PUSH #3
        ADD
        Terminal node:$
        PUSH #2
        PUSH #3
        SUB
    DD x
        PUSH #7
        MOV [x], ST(0)
    DD y
    PUSH #69
    MOV [y], ST(0)
        PUSH #5
        PUSH [x]
        SUB
    Terminal node:<EOF>