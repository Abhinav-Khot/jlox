# Lox Interpreter

A Java interpreter for the Lox programming language.

# Documenation 

The documenation can be found at : https://abhinav-khot.github.io/jlox
## Prerequisites

- Java JDK 8 or higher

## Using Make

### Compile

```console
make
```

Compiles source files into the `bin` directory.

### Start REPL

```console
make run
```

Launches interactive REPL mode for Lox.

### Run Script

```console
make run FILE=path/to/script.lox
```

Executes a Lox script file.

### Clean Build

```console
make clean
```

Deletes the `bin` directory and its contents.

> **Note**: `make run` automatically compiles if needed. You don't need to run `make` first.

## Manual Build

If you prefer not to use Make, you can build and run manually:

### Create Build Directory

```console
mkdir bin
```

### Compile Source

```console
javac -d bin lox/*.java
```

Compiles all Java files from the `lox` directory into `bin`.

### Start REPL

```console
java -cp bin lox.Lox
```

Launches interactive REPL mode for Lox.

### Run Script

```console
java -cp bin lox.Lox path/to/script.lox
```

Executes a Lox script file.
