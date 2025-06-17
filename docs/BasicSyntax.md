# Basic Syntax

# Numbers

Numebrs in lox are **double-precision floating point numbers** by default. Addition / Subtraction / Multiplication and Division is supported natively.

# Strings

Strings in lox are to be enclosed in double-inverted commas. String concatenation is supported through the `+` operator

**Example**

```
var a = "Hello ";
var b = "World";

var c = a + b; // c contains "Hello World"
```

# Variables

Variables are declared and assigned with the following syntax

```
var <variable_name> ;
var <variable_name> = <expresssion>;
```

**Example**

```
var a;
var b = 16;
```

Until the value of the variable is intialized, it holds the value `nil`
/// warn
**Note** : Redeclaring variables is only allowed in the global scope. Any attempt to redeclare variables in a local scope will lead to an error.
///

# Assignment

Variable assignnments are considered to be expressions by lox and thus return the value that is being assigned. This allows for chained assignment.

**Example**
```
var a;
var b;
var c;

c = b = a = 5;
```


# Booleans

Booleans in lox are denoted by `true` and `false`.

### Truthiness

In lox only `nil` and `false` are Falsy values. Every other value is considered to be Truthy.

**Example**:

```
if (12)
{
    print "Yes"; 
} 
// Yes
```

# If-else statements

**Syntax**

```
if (<condition>)
{
    <statements>
}
else
{
    <statements>   
}
```

If the block has only one statement, the braces can be ommitted.

**Example**
```
if (18 >= 7)
{
    print "larger";
}
else print "smaller";
```


# Ternary

Lox provides inline support for branching statements through the ternary operator. 

**Syntax**

```
condition ? expression1 : expression2
```
**Example**

```
var a = 16 > 2 ? 5 : 7;
```

# Print

Lox allows outputting outside the program through `print`.

**Syntax**
```
print <expression>;
```

**Example**
```
print "Hello World";    
```