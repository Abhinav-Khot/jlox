Lox provides support for `while` and `for` style loops. The `for` loops are desugared into `while` loops during evaluation.

## While Loop

**Syntax**
```
while (<condition>)
{
    <statements>
}
```

**Example**

```
//Program that calculates all square numbers form 0 to n - 1

var n = 10;
var squares = [];
var a = 0;

while (a < n)
{
    squares.append(a * a);
    a = a + 1;
}

print squares;
```

## For Loop

**Syntax**
```
for(<intialization> ; <loop_condition> ; <increment / decrement>)
{
    <statements>s
}
```

**Example**

```
for (var a = 0; a < 10; a = a + 1)
{
    print a;   
}
```

## Break

Loops can be exited early using the `break` statement. 

/// warn
Use of the `break` statement anywhere outside loops throws an error.
///

```
for (var a = 0; a < 10; a = a + 1)
{
    print a;
    if (a == 6) break;   
}
```
