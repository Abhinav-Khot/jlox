package lox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lox.TokenType.*;

class Parser
{

    private static class ParseError extends RuntimeException {}

    private final List<Token> tokens;
    private int current = 0;
    private boolean Repl_Mode;

    Parser(List<Token> tokens, boolean repl_mode)
    {
        this.tokens = tokens;
        this.Repl_Mode = repl_mode;
    }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        while(!isAtEnd())
        {
            statements.add(declaration());
        }

        return statements;
      }

    private Stmt declaration()
    {
        try{
          if(match(VAR)) return varDeclaration();
          return statement(); 
        }
        catch(ParseError error)
        {
           synchronize();
           return null;
        }
    }

    private Stmt varDeclaration()
    {
        Token name = consume(IDENTIFIER, "Expected name for the variable.");

        Expr intializer = null;

        if(match(EQUAL))
        {
            intializer = expression();
        }
        consume(SEMICOLON, "Expect ';' after variable declaration");
        return new Stmt.Var(name, intializer);
    }

    private Stmt statement()
    {
        if(match(PRINT)) return printStatement();
        if(match(LEFT_BRACE))
        {
            return new Stmt.Block(block());
        }
        if(match(IF))
        {
            return If();
        }
        if(match(WHILE))
        {
            return While();
        }
        if(match(FOR))
        {
            return For();
        }
        if(match(BREAK))
        {
            return breakStatement();
        }
        return expressionStatement();
    }

    private Stmt printStatement()
    {
        Expr val = expression();
        consume(SEMICOLON, "Expected ';' at the end");
        return new Stmt.Print(val);
    }

    private Stmt expressionStatement()
    {
        Expr val = expression();
        if(Repl_Mode)
        {
            if(!check(SEMICOLON)) return new Stmt.Expression(val);
        }
        consume(SEMICOLON, "Expected ';' at the end");
        return new Stmt.Expression(val);
    }

    private List<Stmt> block()
    {
        List<Stmt> statements = new ArrayList<>();
        while(!check(RIGHT_BRACE) && !isAtEnd())
        {
          statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expected '}' after the block");
        return statements;
    }

    private Stmt If()
    {
       consume(LEFT_PAREN, "Expected '(' after if");
       Expr condition = expression();
       consume(RIGHT_PAREN, "Expected ')' at the end of if condition");

       Stmt trueBranch = statement();
       Stmt falseBranch = null;
       if(match(ELSE))
       {
          falseBranch = statement();
       }

       return new Stmt.If(condition, trueBranch, falseBranch);
    }

    private Stmt While()
    {
        consume(LEFT_PAREN, "Expected '(' after 'while'");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expected ')' after the condition");
        Stmt stmt = statement();

        return new Stmt.While(condition, stmt);
    }

    private Stmt For()
    {
        //we basically "desugar" the for loop into a while loop
        consume(LEFT_PAREN, "Expected '(' after 'for'");
        Stmt initializer;
        if(match(SEMICOLON))
        {
            initializer = null;
        }
        else if(match(VAR))
        {
            initializer = varDeclaration();
        }
        else
        {
            initializer = expressionStatement();
        }

        Expr condition = null;
        if(!check(SEMICOLON))
        {
            condition = expression();
        }
        consume(SEMICOLON, "Expected ; after loop condition");

        Expr increment = null;
        if(!check(SEMICOLON))
        {
            increment = expression();
        }
        consume(RIGHT_PAREN, "Expected ')' after for clauses");

        Stmt body = statement();

        if(increment != null)
        {
            body = new Stmt.Block(Arrays.asList(body, new Stmt.Expression(increment))); //basically append the increment condition to the end of the loop body TODO: In repl mode we enabled printing the value of expressions typed by println value of any expression statemnts in repl mode, but due to this for loops in repl mode prints the result of increment since variable assignment in lox is a expression
        }

        if(condition == null) condition = new Expr.Literal(true);
        body = new Stmt.While(condition, body); //add the loop condition to the beginning of the loop body

        if(initializer != null) //run the intiliazer condition if any once before the while loop starts running
        {
            body = new Stmt.Block(Arrays.asList(initializer, body));
        }

        return body;
    }

    private Stmt breakStatement()
    {
        Token breakTok = previous();
        consume(SEMICOLON, "Expected ';' after 'break'");
        return new Stmt.Break(breakTok);
    }

    private Expr expression()
    {
        return assignment();
    }

    private Expr assignment()
    {
        Expr expr = or();

        if(match(EQUAL))
        {
            Token equals_symbol = previous();
            Expr value = assignment(); //assignment is right associative, thats why we do right recursion here

            if(expr instanceof Expr.Variable)
            {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            }

            error(equals_symbol, "Invalid assignment target."); //the thing we are trying to assign a value to is not a variable
        }

        return expr;
    }

    private Expr or()
    {
        Expr expr = and();

        if(match(OR))
        {
            Token operator = previous();
            Expr right = and();
            
            return new Expr.Logical(expr, operator, right);
        }

        return expr;
    }

    private Expr and()
    {
        Expr expr = ternary();

        if(match(AND))
        {
            Token operator = previous();
            Expr right = ternary();

            return new Expr.Logical(expr, operator, right);
        }

        return expr;
    }
    private Expr ternary() // grammar rule ternary --> equality (? exquality : ternary)*, Notice the beauty : left recursive doesnt work in a recursive descent parsers, but right recusive does ! and ternary is right associative which is implemented by a right recursive rule.
    {
       Expr expr = equality();
       if(match(QUESTION_MARK))
       {
          Expr trueBranch = equality();
          consume(COLON, ": Must be accompanied with the ? (else condition not specified)");
          Expr falseBranch = ternary();
          return new Expr.Ternary(expr, trueBranch, falseBranch);
       }
       return expr;
    }

    private Expr equality()
    {
        Expr expr = comparision();
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new Expr.Binary(expr, operator, right);
          }
      
          return expr;
    }

    private Expr comparision()
    {
        Expr expr = term();

        while(match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL))
        {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();
    
        while (match(MINUS, PLUS)) {
          Token operator = previous();
          Expr right = factor();
          expr = new Expr.Binary(expr, operator, right);
        }
    
        return expr;
    }

    private Expr factor()
    {
        Expr expr = unary();

        while(match(STAR, SLASH))
        {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
          Token operator = previous();
          Expr right = unary();
          return new Expr.Unary(operator, right);
        }
    
        return primary();
    }

    private Expr primary() {
        if (match(FALSE)) return new Expr.Literal(false);
        if (match(TRUE)) return new Expr.Literal(true);
        if (match(NIL)) return new Expr.Literal(null);
    
        if (match(NUMBER, STRING)) {
          return new Expr.Literal(previous().literal);
        }
    
        if (match(LEFT_PAREN)) {
          Expr expr = expression();
          consume(RIGHT_PAREN, "Expect ')' after expression.");
          return new Expr.Grouping(expr);
        }

        if(match(IDENTIFIER)) return new Expr.Variable(previous());

        throw error(peek(), "Expects an expression.");
      }

    private boolean match(TokenType... types)
    {
        for(TokenType type : types)
        {
            if(check(type))
            {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
    
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);  
        return new ParseError();
      }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean check(TokenType type)
    {
        if(isAtEnd())return false;
        if(peek().type == type)return true;
        return false;
    }

    private Token peek()
    {
        return tokens.get(current);
    }

    private boolean isAtEnd()
    {
        return peek().type == EOF;
    }

    private Token previous()
    {
        return tokens.get(current - 1);
    }

    private void synchronize() {
        advance();
    
        while (!isAtEnd()) {
          if (previous().type == SEMICOLON) return;
    
          switch (peek().type) {
            case CLASS:
            case FUN:
            case VAR:
            case FOR:
            case IF:
            case WHILE:
            case PRINT:
            case RETURN:
            case LEFT_BRACE:
            case RIGHT_BRACE: //TODO : check and review misc/skipRightBrace.txt
              return;
          }
    
          advance();
        }
    }
}