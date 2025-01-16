package lox;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class LoxArray extends LoxInstance{

    private final List<Object> elements;
    private static final Map<String, LoxCallable> methods = new HashMap<>();

    LoxArray(List<Object> elements)
    {
        super(null);
        this.elements = elements;

        LoxCallable append_fn =
            new LoxCallable() {
                @Override
                public int arity()
                {
                    return 1;
                }
    
                @Override
                public Object call(Interpreter interpreter, List<Object> arguments)
                {
                    elements.add(arguments.get(0));
                    return null;
                }
    
                @Override
                public String toString()
                {
                    return "<native array fn>"; 
                }
            };

        methods.put("append", append_fn);

        LoxCallable get_fn = 
            new LoxCallable() {
            @Override
            public int arity()
            {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments)
            {
                if(!(arguments.get(0) instanceof Double) || ((Double) arguments.get(0)) % 1 != 0) throw new NativeError("Indexing is integer-based.");
                int index = ((Double) arguments.get(0)).intValue();
                if (index < 0 || index >= elements.size()) throw new NativeError("Index out of bounds.");
                return elements.get(index);
            }

            @Override
            public String toString()
            {
                return "<native array fn>"; 
            }
            };
        
        methods.put("get", get_fn);
    }

    LoxCallable get(Token name)
    {
        if(methods.containsKey(name.lexeme)) return methods.get(name.lexeme);

        throw new RuntimeError(name, "'" + name.lexeme + "' " + "array method does not exist.");
    }

    @Override
    public String toString()
    {
        StringBuilder ret = new StringBuilder();

        ret.append("[");
        for(int i = 0; i < elements.size(); i++)
        {
            ret.append(Interpreter.stringify(elements.get(i)));
            if (i < elements.size() - 1) {
                ret.append(',');
            }
        }
        ret.append(']');
        return ret.toString();
    }
    
}
