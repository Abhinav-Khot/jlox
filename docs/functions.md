## Functions

Functions are defined with the keyword `fun` with the follwing syntax

```
fun <function_name> (<parameters>)
{
    // logic required

    return <expression>;
}

```

An example is:

```
fun add(a, b)
{
    var c = a + b;
    return c;
}
```

## Closures

Lox functions support closures, thus the below program prints `10` appropriately.

```
fun outer()
{
    var x = 10;
    fun inner()
    {
        print x;
    }

    return inner;
}

var f = outer();

f();
```

Lox functions are first-class i.e they can be treated like any other varibale and be passed into functions, be returned by functions and be assigned to some variable. The above example showcases that too.

## Lambdas / Anonymous Functions

Lox supports anonymous functions too. The syntax remains mostly the same with the exception of the function name not being included in the definition. These lambdas are considered expressions by lox.

**Syntax**

```
fun (<parameters>) {
    <statements>
}
```

**Example**

```
var fibonacci = fun (a){
    if (a == 0) return 0;
    if (a == 1 or a == 2) return 1;
    
    return fibonacci(a - 1) + fibonacci(a - 2);
};

print fibonacci(10);    
```


/// warn
Using `return` outside a function or method throws an error.
///

# Native functions

## `clock`

Returns the time elapsed since the program started execution in seconds.
