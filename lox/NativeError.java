//native functions dont usually have access to the tokens since we directly use LoxCallable for them, so we cant throw RuntimeError in such cases.
//this is the usecase.

package lox;

public class NativeError extends RuntimeException{
    NativeError(String message)
    {
        super(message);
    }
}
