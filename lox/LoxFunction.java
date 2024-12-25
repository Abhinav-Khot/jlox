package lox;
import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;

    LoxFunction(Stmt.Function decl)
    {
        this.declaration = decl;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> Arguments)
    {
        Environment environment = new Environment(interpreter.globals);

        for(int i = 0; i < declaration.parameters.size() ; i ++)
        {
            environment.define(declaration.parameters.get(i).lexeme, Arguments.get(i));
        }

        interpreter.executeBlock(declaration.body, environment);

        return null;
    }

    @Override
    public int arity()
    {
        return declaration.parameters.size();
    }

    @Override
    public String toString()
    {
        return "<fn " + declaration.name.lexeme + " >";
    }
}
