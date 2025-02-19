//PURELY A UTILITY file to automate the creation of lox/Expr.java

package tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class generateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
          System.err.println("Usage: generate_ast <output directory>");
          System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
        "Assign  : Token name, Expr value",
        "Binary   : Expr left, Token operator, Expr right",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Logical  : Expr left, Token operator, Expr right",
        "Unary    : Token operator, Expr right",
        "Ternary  : Expr Condition, Expr TrueBranch, Expr FalseBranch",
        "Variable : Token name",
        "This     : Token keyword",
        "Super    : Token keyword, Token method",
        "Call     : Expr calee, Token paren, List<Expr> arguments", //paren will be used to store token of right parenthesis, would be uselful for error reporting
        "Get      : Expr object, Token name",
        "Set      : Expr object, Token name, Expr value",
        "AnonymousFunction : List<Token> parameters, List<Stmt> body" ,
        "Array    : List<Expr> elements" 
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
            "Block : List<Stmt> Statements",
        "Expression : Expr expression",
        "If : Expr condition, Stmt trueBranch, Stmt falseBranch",
        "Print      : Expr expression",
        "Var        : Token name, Expr intializer",
        "Class      : Token name , Expr.Variable superclass, List<Stmt.Function> methods, List<Stmt.Function> staticmethods",
        "While      : Expr condition, Stmt body",
        "Break      : Token breakToken",
        "Function   : Token name, List<Token> parameters, List<Stmt> body",
        "Return     : Token keyword, Expr value" //keyword stores the 'break' token, it will be useful for error reporting.
        ));
      }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        
        defineVisitor(writer, baseName, types);

        // The AST classes.
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            System.out.println(className + " " + fields); 
            defineType(writer, baseName, className, fields);
        }

        // The base accept() method.
        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");


        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
            writer.println("  interface Visitor<R> {");

            for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                typeName + " " + baseName.toLowerCase() + ");");
            }

            writer.println("  }");
    }
    private static void defineType( PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("  static class " + className + " extends " +
            baseName + " {");

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
        String name = field.split(" ")[1];
        writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Visitor pattern.
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
            className + baseName + "(this);");
        writer.println("    }");


        // Fields.
        writer.println();
        for (String field : fields) {
        writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
        
}
