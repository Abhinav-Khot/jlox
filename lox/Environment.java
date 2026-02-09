package lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    final Environment enclosing;

    Environment()
    {
        this.enclosing = null;
    }
    Environment(Environment encl) //define the immediately enclosing scope.
    {
        this.enclosing = encl;
    }
    void define(String name, Object val)
    {
        values.put(name, val);
    }

    Object getAt(Integer distance, String name)
    {
        Environment environment = this;
        for(int i = 0; i < distance; i ++)
        {
            environment = environment.enclosing;
        }

        return environment.values.get(name);
    }

    void assignAt(Integer distance, Token name, Object value)
    {
        Environment environment = this;
        for(int i = 0; i < distance; i ++)
        {
            environment = environment.enclosing;
        }

        environment.values.put(name.lexeme, value);
    }

    Object get(Token name)
    {
        if(values.containsKey(name.lexeme))
        {
            return values.get(name.lexeme);
        }

        if(enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");

    }

    void assign(Token name, Object val)
    {
        if(values.containsKey(name.lexeme))
        {
            values.put(name.lexeme, val);
            return;
        }
        if(enclosing != null)
        {
            enclosing.assign(name, val);
            return;
        }
        throw new RuntimeError(name, "Undefined varibale '" + name.lexeme + "'.");
    }
}
