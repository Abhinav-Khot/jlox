package lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LoxClass implements LoxCallable{
  final String name;
  final Map<String, LoxFunction> methods;
  LoxClass(String name, Map<String, LoxFunction> methods) {
    this.name = name;
    this.methods = methods;
  }

  LoxFunction findMethod(String name)
  {
        if(methods.containsKey(name)) return methods.get(name);

        return null;
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
