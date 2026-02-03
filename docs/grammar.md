The main structure of lox's grammar is described as follows:

```
program        → (declaration)* EOF

declaration    → classDecl | funDecl | varDecl | statement

classDecl      → "class" IDENTIFIER ("<" IDENTIFIER)?
                 "{" ( ("static")? function)* "}"

funDecl        → "fun" function

function       → IDENTIFIER "(" parameters? ")" block

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

block          → "{" declaration* "}"
```

```
expression     → assignment ;

assignment     → ( call "." )? IDENTIFIER "=" assignment | ternary

ternary        → logic_or ("?" ternary ":" ternary)

logic_or       → logic_and ( "or" logic_and )*

logic_and      → equality ( "and" equality )*

anonymous_func → "FUN" function ";" | equality

equality       → comparison ( ( "!=" | "==" ) comparison )*

comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )*

term           → factor ( ( "-" | "+" ) factor )*

factor         → unary ( ( "/" | "*" ) unary )*

unary          → ( "!" | "-" ) unary | call

call           → primary ( "(" arguments? ")" | "." IDENTIFIER )*

primary        → "true" | "false" | "nil" | "this"
               | NUMBER | STRING | IDENTIFIER | "(" expression ")"
               | "super" "." IDENTIFIER | "[" elements? "]"

arguments      → expression ("," expression)*

elements       → expression ("," expression)*
```
