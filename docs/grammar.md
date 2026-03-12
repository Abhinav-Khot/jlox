The main structure of lox's grammar is described as follows:

```
program        → (declaration)* EOF

declaration    → classDecl | funDecl | varDecl | statement

classDecl      → "class" IDENTIFIER ("<" IDENTIFIER)?
                 "{" ( ("static")? function)* "}"

funDecl        → "fun" IDENTIFIER function

function       →  "(" parameters? ")" block

parameters     → IDENTIFIER ("," IDENTIFIER)*

varDecl        → "var" IDENTIFIER ( "=" expression )? ";"
```

```
statement      → exprStmt | forStmt | ifStmt | printStmt | returnStmt | whileStmt | breakStmt | block ;

exprStmt       → expression ";"

forStmt        → "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement

ifStmt         → "if" "(" expression ")" statement
                  ( "else" statement )?

printStmt      → "print" expression ";"

returnStmt     → "return" expression? ";"

whileStmt      → "while" "(" expression ")" statement

breakStmt      → "break" ";"

breakStmt      → "break" ";"

block          → "{" declaration* "}"
```

```
expression     → assignment ;

assignment     → ( call_or_access "." )? IDENTIFIER "=" assignment | ternary

ternary        → logic_or ("?" ternary ":" ternary)

logic_or       → logic_and ( "or" logic_and )*

logic_and      → equality ( "and" equality )*

equality       → comparison ( ( "!=" | "==" ) comparison )*

comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )*

term           → factor ( ( "-" | "+" ) factor )*

factor         → unary ( ( "/" | "*" ) unary )*

unary          → ( "!" | "-" ) unary | call_or_access 

call_or_access → primary ( "(" arguments? ")" | "." IDENTIFIER )* 

primary        → "true" | "false" | "nil" | "this" | anonFunc |
               | NUMBER | STRING | IDENTIFIER | "(" expression ")"
               | "super" "." IDENTIFIER | "[" elements? "]"

anonymous_func → "lambda" function

arguments      → expression ("," expression)*

elements       → expression ("," expression)*
```
