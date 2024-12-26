package lox;

public class Return extends RuntimeException
{
    final Object value;
    final Token token;

    Return(Object value, Token token)
    {
        super("'return' can only be used inside function/method body.");
        this.value = value;
        this.token = token;
    }
}