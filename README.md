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

### Compile

```
javac lox/*.java
```

### Run REPL

```
java lox.Lox
```

### Run Script

```
java lox.Lox path/to/script.lox
```
