grammar first;

prog:	stat* EOF ;

stat: expr #expr_stat
    | 'zmienna' varName=ID ('=' varVal=expr)? #var_def
    | IF_kw '(' cond=expr ')' then=block  ('else' else=block)? #if_stat
    | WHILE_kw '(' cond=expr ')' loopBody=block # whileLoop
    | '$' expr #print_stat
    ;

block : stat #block_single
    | '{' block* '}' #block_real
    ;

expr:
    	l=expr op=(ADD|SUB) r=expr #binOp
    |   l=expr op=(MUL|DIV) r=expr #binOp
    |   NOT r=expr #unOp
    |   l=expr op=(EQ|NEQ|LESS|GREAT|GREATEQ|LESSEQ) r=expr #arOp
    |   l=expr op=(AND|OR) r=expr #arOp
    |	INT #int_tok
    |   ID #id_tok
    |	'(' expr ')' #pars
    | <assoc=right> ID '=' expr # assign
    | expr ',' expr #list // OBSŁUGA LISTY (najniższy priorytet)
    ;

IF_kw : 'if' ;

DIV : '/' ;

MUL : '*' ;

SUB : '-' ;

ADD : '+' ;

SEMI : ';' ;

COMMA: ','; // DODANE: Token dla przecinka

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
