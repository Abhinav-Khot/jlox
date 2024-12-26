package lox;

import java.util.List;

public class LoxAnonymousFunction implements LoxCallable{
    private final Expr.AnonymousFunction declaration;
    private final Environment closure;

    LoxAnonymousFunction(Expr.AnonymousFunction decl, Environment closure)
    {
        this.declaration = decl;
        this.closure = closure;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> Arguments)
    {
        Environment environment = new Environment(closure);

        for(int i = 0; i < declaration.parameters.size() ; i ++)
        {
            environment.define(declaration.parameters.get(i).lexeme, Arguments.get(i));
        }
        try
        {
            interpreter.executeBlock(declaration.body, environment);
        }
        catch(Return ret)
        {
            return ret.value;
        }
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
        return "Anonymous function";
    }    
}
