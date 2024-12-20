package lox;
import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void>{

    public boolean Mode_REPL = false;
    private Environment environment = new Environment();

    class RuntimeError extends RuntimeException
    {
        final Token token;

        RuntimeError(Token token, String message)
        {
            super(message);
            this.token = token;
        }
    }

    void interpret(List<Stmt> statements, boolean repl_mode)
    {
        this.Mode_REPL = repl_mode;
        try 
        {
            for(Stmt statement : statements)
            {
                execute(statement);
            }
        }
        catch(RuntimeError Err)
        {
            Lox.runtimeError(Err);
        }
    }

    private Void execute(Stmt stmt)
    {
        stmt.accept(this);
        return null;
    }

    
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt)
    {
        if(Mode_REPL)System.out.println(stringify(evaluate(stmt.expression)));
        else evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt)
    {
        Object val = evaluate(stmt.expression);
        System.out.println(stringify(val));
        return null;
    }

    @Override 
    public Void visitVarStmt(Stmt.Var stmt)
    {
        Object value = null;
        if(stmt.intializer != null)
        {
            value = evaluate(stmt.intializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }


    @Override
    public Object visitLiteralExpr(Expr.Literal expr)
    {
        return expr.value;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr)
    {
        return evaluate(expr.expression);
    }

    public Object evaluate(Expr expr)
    {
        return expr.accept(this);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr)
    {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                return -(double)right; //even if we return double, java autoboxes it to the Double class which is a subclass of Object 
        
            case BANG: // false and null are false, everything else is true ;)
                if (right == null) return false;
                if(right instanceof Boolean) return (boolean)right;
                return true;

            default:
                return null;
        }
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary expr)
    {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch(expr.operator.type)
        {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return (double)left - (double)right;

            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;

            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                if((double)right == 0) throw new RuntimeError(expr.operator, "Division by Zero is not permitted");
                return (double)left / (double)right;

            case PLUS: //TODO : concantenation of a number with a string causes even the leading .0 to be present, remove that eg : 8 + b = 8.0b
                if(left instanceof String && right instanceof String) return (String)left + (String)right; //even if one of the operators is a string, concatenate them
                
                if(left instanceof Double && right instanceof Double) return (double)left + (double)right; //add only if both are double

                if(left instanceof String && right instanceof Double) return (String)left + right.toString();

                if(left instanceof Double && right instanceof String) return left.toString() + (String)right;
                
                throw new RuntimeError(expr.operator, "Operands must be either numbers or strings");
            
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left,right);
            default:
                return null;
        }
    }

    @Override 
    public Object visitTernaryExpr(Expr.Ternary expr)
    {
        Object condition = evaluate(expr.Condition);
        boolean cond;
        if(condition == null) cond = false;
        else if(condition instanceof Boolean) cond = (boolean)condition;
        else cond = true;

        if(cond == false) return evaluate(expr.FalseBranch);
        else return evaluate(expr.TrueBranch);

    }

    @Override 
    public Object visitVariableExpr(Expr.Variable expr)
    {
        return environment.get(expr.name);
    }

    private boolean isEqual(Object left, Object right)
    {
        if(left == null && right == null)return true;
        if(left == null || right == null)return false;

        return left.equals(right);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private String stringify(Object val)
    {
        if(val == null)return "nil";
        if(val instanceof Double) //cosmetic to remove trailing .0
        {
            String printed = val.toString();
            if(printed.endsWith(".0"))return printed.substring(0, printed.length() - 2);
            return printed;
        }
        return val.toString();
    }

}

