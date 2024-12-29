package lox;
import java.util.List;

public class LoxFunction implements LoxCallable {
    private final Stmt.Function declaration;
    private final Environment closure;

    LoxFunction(Stmt.Function decl, Environment closure)
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

    LoxFunction bind(LoxInstance instance)
    {
        Environment env = new Environment(closure);
        env.define("this", instance);
        return new LoxFunction(declaration, env);
    }

    @Override
    public int arity()
    {
        return declaration.parameters.size();
    }

    @Override
    public String toString() //toSting() is a fucntion provided by the Object class in java which is the superclass of all classes. So we can override it even though LoxCallable doesnt have it.
    {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
