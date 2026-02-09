package lox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

class ByteCodeGen implements Expr.Visitor<List<Instruction>>, Stmt.Visitor<List<Instruction>>{

    ByteCodeGen()
    {

    }

    @Override
    public List<Instruction> visitLiteralExpr(Expr.Literal expr) {
        List<Instruction> ret = new java.util.ArrayList<>();
        List<Object> args = new java.util.ArrayList<>();
        args.add(expr.value);
        ret.add(new Instruction(OpCode.PUSH_COSNT, args));
        return ret;
    }

    @Override
    public List<Instruction> visitBreakStmt(Stmt.Break stmt) {
        List<Instruction> ret = new java.util.ArrayList<>();
        List<Object> args = new java.util.ArrayList<>();
        args.add(0);
        ret.add(new Instruction(OpCode.BREAK, args));
        return ret;
    }

    @Override
    public List<Instruction> visitSuperExpr(Expr.Super expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitThisExpr(Expr.This expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitClassStmt(Stmt.Class stmt) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitAnonymousFunctionExpr(Expr.AnonymousFunction expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitUnaryExpr(Expr.Unary expr) {
        List<Instruction> ret = new ArrayList<>();

        ret.addAll(compile(expr.right));
        ret.add(new Instruction(expr.operator.type == TokenType.BANG ? OpCode.NOT : OpCode.UNARY_MINUS, new ArrayList<>()));
        return ret;
    }

    @Override
    public List<Instruction> visitReturnStmt(Stmt.Return stmt) {
        List<Instruction> ret = new ArrayList<>();
        ret.add(new Instruction(OpCode.RETURN, new ArrayList<>()));
        return ret;
    }

    @Override
    public List<Instruction> visitPrintStmt(Stmt.Print stmt) {
        List<Instruction> ret = new ArrayList<>();
        ret.addAll(compile(stmt.expression));
        ret.add(new Instruction(OpCode.PRINT, new ArrayList<>()));
        return ret;
    }

    @Override
    public List<Instruction> visitArrayExpr(Expr.Array expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitBlockStmt(Stmt.Block stmt) {
        List<Instruction> ret = new ArrayList<>();
        ret.add(new Instruction(OpCode.BEGIN_BLOCK, null));
        ret.addAll(compile(stmt.Statements));
        ret.add(new Instruction(OpCode.END_BLOCK, null));
        return ret;
    }

    @Override
    public List<Instruction> visitVariableExpr(Expr.Variable expr) {
        List<Instruction> ret = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        args.add(expr.name);
        ret.add(new Instruction(OpCode.LOAD_NAME, args));

        return ret;
    }

    @Override
    public List<Instruction> visitIfStmt(Stmt.If stmt) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitGetExpr(Expr.Get expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitCallExpr(Expr.Call expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitTernaryExpr(Expr.Ternary expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitSetExpr(Expr.Set expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitBinaryExpr(Expr.Binary expr) {
        List<Instruction> ret = new java.util.ArrayList<>();
        ret.addAll(compile(expr.left));
        ret.addAll(compile(expr.right));

        Map<TokenType, OpCode> map = new HashMap<>();
        map.put(TokenType.PLUS, OpCode.ADD);
        map.put(TokenType.MINUS, OpCode.SUB);
        map.put(TokenType.STAR, OpCode.MUL);
        map.put(TokenType.SLASH, OpCode.DIV);

        ret.add(new Instruction(map.get(expr.operator.type), new ArrayList<>()));
        return ret;
    }

    @Override
    public List<Instruction> visitVarStmt(Stmt.Var stmt) {
        List<Instruction> ret = new ArrayList<>();
        List<Object> args = new ArrayList<>();
        args.add(stmt.name);
        ret.addAll(compile(stmt.intializer));
        ret.add(new Instruction(OpCode.DEFINE_NAME, args));
        return ret;
    }

    @Override
    public List<Instruction> visitGroupingExpr(Expr.Grouping expr) {
        return compile(expr.expression);
    }

    @Override
    public List<Instruction> visitWhileStmt(Stmt.While stmt) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitAssignExpr(Expr.Assign expr) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitExpressionStmt(Stmt.Expression stmt) {
        List<Instruction> ret = compile(stmt.expression);
        ret.add(new Instruction(OpCode.POP_VALUE, null));
        return ret;
    }

    @Override
    public List<Instruction> visitFunctionStmt(Stmt.Function stmt) {
        return new java.util.ArrayList<>();
    }

    @Override
    public List<Instruction> visitLogicalExpr(Expr.Logical expr) {
        List<Instruction> ret = new java.util.ArrayList<>();

        ret.addAll(compile(expr.left));
        ret.addAll(compile(expr.right));
        ret.add(new Instruction(expr.operator.type == TokenType.OR ? OpCode.LOGICAL_OR : OpCode.LOGICAL_AND, new ArrayList<>()));

        return ret;
    }

    List<Instruction> compile(Expr expr)
    {
        return expr.accept(this);
    }

    List<Instruction> compile(Stmt stmt)
    {
        return stmt.accept(this);
    }

    List<Instruction> compile(List<Stmt> statements)
    {
        List<Instruction> ret = new ArrayList<>();
        for (Stmt s : statements) ret.addAll(compile(s));
        return ret;
    }
    public static void main(String[] args)
    {
        Scanner s = new Scanner("var a = 5; a; {var a = 100; a;} print a;");
        Parser p = new Parser(s.scanTokens(), false);

        ByteCodeGen b = new ByteCodeGen();

        // Expr left = new Expr.Literal(3.0);
        // Expr right = new Expr.Literal(5.0);
        // Expr.Binary test = new Expr.Binary(left, new Token(TokenType.PLUS, "+", null, 1), right);

        List<Instruction> req = b.compile(p.parse());
        VirtualMachine v = new VirtualMachine(req);

        v.evaluate();
    }
}
