grammar first;

prog:	(stat|func_def)* EOF ;

stat: expr #expr_stat
    | 'zmienna' varName=ID ('=' varVal=expr)? #var_def
    | IF_kw '(' cond=expr_log ')' then=block  ('else' else=block)? #if_stat
    | WHILE_kw '(' cond=expr ')' loopBody=block # whileLoop
    | '$' expr #print_stat
    ;

func_def: 'func' name=ID '(' par+=ID (',' par+=ID)* ')' func_body=block;
func_call: name=ID '(' arg +=expr (',' arg+=expr)* ')';

expr_log: NOT r=expr #logic_not
          |   l=expr op=(EQ|NEQ) r=expr #logic_comp
          |   l=expr op=(LESS|GREAT|GREATEQ|LESSEQ) r=expr #logic_comp
          |   INT #logic_int_tok
          |   ID #logic_id_tok;

block : stat #block_single
    | '{' block* '}' #block_real
    ;

expr:   l=expr op=(MUL|DIV) r=expr #binOp
    |	l=expr op=(ADD|SUB) r=expr #binOp
    |   NOT r=expr #unOp
    |   l=expr op=(EQ|NEQ|LESS|GREAT|GREATEQ|LESSEQ) r=expr #arOp
    |   l=expr op=(AND|OR) r=expr #arOp
    |	INT #int_tok
    |   ID #id_tok
    |   func_call #funcCall // wywołanie funkcji
    |	'(' expr ')' #pars
    |   <assoc=right> ID '=' expr # assign
//    |   expr ',' expr #list // OBSŁUGA LISTY (najniższy priorytet)
    ;

IF_kw : 'if' ;

DIV : '/' ;

MUL : '*' ;

SUB : '-' ;

ADD : '+' ;

SEMI : ';' ;

COMMA: ',';

EQ : '==';
NEQ : '!=';
LESS : '<';
GREAT : '>';
LESSEQ : '<=';
GREATEQ : '>=';
AND : 'and';
OR : 'or';
NOT : 'not';

WHILE_kw : 'while';

//NEWLINE : [\r\n]+ -> skip;
NEWLINE : [\r\n]+ -> channel(HIDDEN);

//WS : [ \t]+ -> skip ;
WS : [ \t]+ -> channel(HIDDEN) ;

INT     : [0-9]+ ;

ID : [a-zA-Z_][a-zA-Z0-9_]* ;

COMMENT : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~'\n'* '\n' -> channel(HIDDEN) ;
