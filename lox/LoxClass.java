package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoxClass extends LoxInstance implements LoxCallable{
  final String name;
  final Map<String, LoxFunction> methods;
  final Map<String, LoxFunction> staticMethods;
  final LoxClass superclass;

  LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods, Map<String, LoxFunction> staticMethods) {
    super(null);
    this.name = name;
    this.methods = methods;
    this.staticMethods = staticMethods;
    this.superclass = superclass;
  }

  LoxFunction findMethod(String name)
  {
        if(methods.containsKey(name)) return methods.get(name);
        if(superclass != null)
        {
            return superclass.findMethod(name);
        }
        return null;
  }

  LoxFunction get(Token name) //to handle static method calls
  {
      if(staticMethods.containsKey(name.lexeme)) return staticMethods.get(name.lexeme);

      throw new RuntimeError(name, "Undefined propery'" + name.lexeme + "' .");
  }

  @Override
  public Object call(Interpreter interpreter, List<Object> arguments)
  {
     LoxInstance instance = new LoxInstance(this);
     LoxFunction intializer = findMethod("init");
     if(intializer != null)
     {
        intializer.bind(instance).call(interpreter, arguments);
     }
     return instance;
  }

  @Override
  public int arity()
  {
    LoxFunction intializer = findMethod("init");
    if(intializer != null) return intializer.arity();
    return 0;
  }
  @Override
  public String toString() { //toSting() is a fucntion provided by the Object class in java which is the superclass of all classes. So we can override it even though LoxCallable doesnt have it.
    return name;
  }
}
