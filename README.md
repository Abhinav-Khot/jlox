# Lox Interpreter

A Java implementation of the Lox programming language.

## Requirements

- Java JDK 8 or higher

## Building and Running with Make

### Compile

```
make
```

### Run REPL

```
make run
```

### Run Script

```
make run FILE=path/to/script.lox
```

### Clean Build

```
make clean
```

## Run without make

```
mkdir bin
```

### Compile

```
javac -d bin lox/*.java
```

### Run REPL

```
java -cp bin lox.Lox
```

### Run Script

```
java -cp bin lox.Lox path/to/script.lox
```
