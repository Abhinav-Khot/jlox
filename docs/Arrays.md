Lox supports heterogeneous dynamic arrays i.e the array elements can be any lox defined value, like primitve `int`, `bools`, `objects`, `functions` etc.

## Definition 

An array is created with the following syntax:
```
[value1, value2, ......]
```

There are three main methods provided by lox for arrays:

## `append(val)`
Adds a value to the end of the existing array

**Example** :
```
var a = [1, 2, 3];
a.append(4);
print a; // [1, 2, 3, 4]
```

## `get(index)`
Returns the value at the specified index.

///warn
If index is out of bounds, an error is thrown.
///

**Example** :
```
var a = [23, 2, 3];
print a.get(0); // 23
```
    
## `update(index, val)`
Replaces the value at index `index` with `val`.

///warn
If index is out of bounds, an error is thrown.
///

**Example** :
```
var a = [23, 2, 3];
a.update(1, 90);
print a.get(1); // 90
```


