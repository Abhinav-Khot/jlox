package lox;

public class BreakError extends RuntimeException {
    final Token token;
    
    BreakError(Token breakToken)
    {
        super("break statement cannot be used outside a loop.");
        this.token = breakToken;
    }
}

//basically we throw a BreakError when we encounter a break, if this break is in a loop it is caught otherwise it travels upwards and is reported as an error - "break appears outside loop"
