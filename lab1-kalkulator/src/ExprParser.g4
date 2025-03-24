parser grammar ExprParser;
options { tokenVocab=ExprLexer; }

program
    : stat EOF
    | def EOF
    ;

stat: ID '=' expr ';'
    | expr ';'
    ;

def : ID '(' ID (',' ID)* ')' '{' stat* '}' ;

expr: ID
    | INT
    | func
    | 'not' expr
    | expr 'and' expr
    | expr 'or' expr
    | '(' expr ')' // Add parentheses
    | expr ('*'|'/') expr // Add arithmetic operators
    | expr ('+'|'-') expr // Add arithmetic operators
    ;

// dodawanie odejmowanie - ten sam priorytet
// mnozenie dzielenie - ten sam priorytet
// poprawic
// expr: expr ('+'|'-') expr

func : ID '(' expr (',' expr)* ')' ;