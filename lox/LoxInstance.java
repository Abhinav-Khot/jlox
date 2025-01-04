package lox;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {
    private LoxClass klass; //eh a bit of quirky naming
    private final Map<String, Object> fields = new HashMap<String, Object>();
    
    LoxInstance(LoxClass klass)
    {
        this.klass = klass;
    }

    Object get(Token name)
    {
        if(fields.containsKey(name.lexeme)) return fields.get(name.lexeme);

        LoxFunction method = klass.findMethod(name.lexeme);

        if(name.lexeme.equals("init")) throw new RuntimeError(name, "Initializer cannot be invoked explicitly after instance has been constructed."); //we dont want users to invoke init() on their own
        
        if(method != null) return method.bind(this);

        LoxFunction staticMethod = klass.get(name);
        if(staticMethod != null) return staticMethod; //Lox allows calling static methods through instances for now.

        else throw new RuntimeError(name, "Undefined Property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value)
    {
        fields.put(name.lexeme, value);
    }


    @Override
    public String toString()
    {
        return klass.name + " instance.";
    }
}
