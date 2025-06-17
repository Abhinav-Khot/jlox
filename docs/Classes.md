# Classes

# Syntax

Class in lox can be defined with the following syntax:

```
class <class_name> < <parentclass_name>
{

    init(<parameters>) //the constructor
    {

    }

    method_1(<parameters>)
    {
        ...
    }
    .
    .
    .
    static smethod_1(<parameters>)
    {
        ....
    }

}
```
# This

To refer to the current object in the methohds, the keyword `this` can be used. Using `this` in static methods will throw an error.

# Constructor

The constructor of the class can be defined through `init`. Each of the methods of the classes are defined the same way functions are defined with the only change being that we dont use the `fun` keyword. 

Note that in case of a class having a parent class, when the object for the class is initialized, only the constructor for the base class is called automatically. If one wishes to call the constructor of the parent class, they can do so with `super` . An example of this is shown in the `Inheritance` section.

# Static methods

Lox has support for static methods, which can directly be called from the class without instantiating an object for that class. All such methods must be prefixed with the `static`.

**Example:**

```
class Math{
    init()
    {
        print "math module initialized";
    }

    static square(a){
         return a * a;
    }

    static min(a, b){
        if (a < b) return a;
        return b;
    }

    static max(a, b){
        if (a > b) return a;
        return b;
    }

    static abs(a){
        return a > 0 ? a : -a;
    }
}

print Math.square(6);
print Math.min(10, 5);
print Math.max(782, 37349);
print Math.abs(-92);

```

# Inheritance

Lox has support for single - inheritance. During the definintion of the class the parent class is to be specified after the `<` token. All the methods of the parent class including the constructor are available to the child class. When a method is called from the child class / object, first the method is searched in the sub class and then the super class.

 Suppose two methods with the same name exist in both the subclass and the superclass and a user wants to call the method in the superclass directly, `super` can be used.


**Example**

```
class Vehicle{
    init(name)
    {
        this.name = name;
    }

    forward()
    {
        print this.name + " Moves forward";
    }
}

class Car < Vehicle{
    init(name)
    {
        super.init(name);
    }
    
    wheels()
    {
        print this.name + " Has four wheels";
    }
}

class Bike < Vehicle{
    init(name)
    {
        super.init(name);
    }

    wheels()
    {
        print this.name + " Has two wheels";
    }
}

var honda = Bike("Honda");
honda.forward();
honda.wheels();

var mclaren = Car("McLaren");
mclaren.forward();
mclaren.wheels();
```
///important
 lox forbids the use of `super` in static methods.
///

///warn
 When an object is created `init` is automatically called for the base class and if it is called again explicitly by the user, an error is thrown.
///