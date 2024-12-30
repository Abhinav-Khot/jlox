# Lox Interpreter

A Java implementation of the Lox programming language.

## Prerequisites

- Java JDK 8 or higher

## Using Make

### Compile

```bash
make
```

Compiles source files into the `bin` directory.

### Start REPL

```bash
make run
```

Launches interactive REPL mode for Lox.

### Run Script

```bash
make run FILE=path/to/script.lox
```

Executes a Lox script file.

### Clean Build

```bash
make clean
```

Deletes the `bin` directory and its contents.

> **Note**: `make run` automatically compiles if needed. You don't need to run `make` first.

## Manual Build

If you prefer not to use Make, you can build and run manually:

### Create Build Directory

```bash
mkdir bin
```

### Compile Source

```bash
javac -d bin lox/*.java
```

Compiles all Java files from the `lox` directory into `bin`.

### Start REPL

```bash
java -cp bin lox.Lox
```

Launches interactive Lox interpreter.

### Run Script

```bash
java -cp bin lox.Lox path/to/script.lox
```

Executes a Lox script file.
