package lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    
    void define(String name, Object val)
    {
        values.put(name, val);
    }

    Object get(Token name)
    {
        if(values.containsKey(name.lexeme))
        {
            return values.get(name.lexeme);
        }
        
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
        
    }
}
