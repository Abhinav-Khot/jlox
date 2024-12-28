package lox;

public class Break extends RuntimeException {
    final Token token;
    
    Break(Token breakToken)
    {
        super("break statement cannot be used outside a loop.");
        this.token = breakToken;
    }
}

//basically we throw a BreakError when we encounter a break, if this break is in a loop it is caught
