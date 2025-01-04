package lox;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Stack;

class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void>{
    private final Interpreter interpreter;
    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private FunctionType currFuntion = FunctionType.NONE;
    private ClassType currClass = ClassType.NONE;

    private boolean inLoop = false;

    Resolver(Interpreter interpreter)
    {
        this.interpreter = interpreter;
    }

    private enum FunctionType
    {
        NONE, FUNCTION, ANONYMOUSFUNCTION, METHOD, INITIALIZER, STATICMETHOD
    }

    private enum ClassType
    {
        NONE, CLASS, SUBCLASS
    }

    void resolve(Stmt stmt)
    {
        stmt.accept(this);
    }

    void resolve(Expr expr)
    {
        expr.accept(this);
    }

    void resolve(List<Stmt> statements)
    {
        for(Stmt statement : statements)
        {
            resolve(statement);
        }
    }

    @Override
    public Void visitBlockStmt(Stmt.Block stmt)
    {
        beginScope();
        resolve(stmt.Statements);
        endScope();
        return null;

    }

    private void beginScope()
    {
        scopes.push(new HashMap<String, Boolean>());    
    }

    private void endScope()
    {
        scopes.pop();
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt)
    {
        declare(stmt.name);
        if(stmt.intializer != null)
        {
            resolve(stmt.intializer);
        }
        define(stmt.name);
        return null;
    }

    private void declare(Token name)
    {
        if(scopes.isEmpty()) return;

        if(scopes.peek().containsKey(name.lexeme))
        {
            Lox.error(name, "A variable with this name already exists in this scope");
        }
        scopes.peek().put(name.lexeme, false);
    }

    private void define(Token name)
    {
        if(scopes.isEmpty())return;
        scopes.peek().put(name.lexeme, true); 
    }

    @Override
    public Void visitAssignExpr(Expr.Assign expr)
    {
        resolve(expr.value);
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Stmt.Function stmt) {
      declare(stmt.name);
      define(stmt.name);

      resolveFunction(stmt, FunctionType.FUNCTION);
      return null;
    }
    
    private void resolveFunction(Stmt.Function function, FunctionType type) {
        FunctionType enclosing = currFuntion;
        currFuntion = type;
        beginScope();
        for (Token param : function.parameters) {
          declare(param);
          define(param);
        }
        resolve(function.body);
        endScope();
        currFuntion = enclosing;
    }   
    
    @Override
    public Void visitClassStmt(Stmt.Class stmt)
    {
        ClassType prev = this.currClass;
        this.currClass = ClassType.CLASS; //to detect invalid uses of 'this'
        declare(stmt.name);
        define(stmt.name);
        if(stmt.superclass != null && stmt.superclass.name.lexeme.equals(stmt.name.lexeme))
        {
            Lox.error(stmt.superclass.name, "A class cannot inherit from itself.");
        }
        if(stmt.superclass != null)
        {
            this.currClass = ClassType.SUBCLASS;
            resolve(stmt.superclass);
            beginScope(); //for handling 'super'.
            scopes.peek().put("super", true);
        }
        for(Stmt.Function staticmethod : stmt.staticmethods)
        {
            FunctionType declaration = FunctionType.STATICMETHOD;
            resolveFunction(staticmethod, declaration);
        }
        beginScope();
        scopes.peek().put("this", true);
        for(Stmt.Function method : stmt.methods)
        {
            FunctionType declaration = FunctionType.METHOD;
            if(method.name.lexeme.equals("init")) declaration = FunctionType.INITIALIZER;
            resolveFunction(method, declaration);
        }
        endScope();
        if(stmt.superclass != null) endScope();
        this.currClass = prev;
        return null;    
    }
    
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt)
    {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt)
    {
        resolve(stmt.condition);
        resolve(stmt.trueBranch);
        if(stmt.falseBranch != null) resolve(stmt.falseBranch);

        return null;
    }

    @Override
    public Void visitPrintStmt(Stmt.Print stmt)
    {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt)
    {
        boolean enclosing = inLoop;
        resolve(stmt.condition);
        inLoop = true;
        resolve(stmt.body);
        inLoop = enclosing;
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt)
    {
        if(currFuntion == FunctionType.NONE) 
        {
            Lox.error(stmt.keyword, "Cannot return from top-level code.");
            return null; //return is not at the right location, why continue resolving the expression to be returned? This is a design choice.
        }
        if(stmt.value != null) 
        {
            if(currFuntion == FunctionType.INITIALIZER)
            {
                Lox.error(stmt.keyword, "Cannot return a value from an initializer.");
                return null; //return in a intitialiser shouldnt have any value, so we dont care about reporting any resoluutionerrors in the expression that the user has specified to be returned. Again a design choice.
            }
            resolve(stmt.value);
        }
        return null;
    }

    @Override
    public Void visitBreakStmt(Stmt.Break stmt)
    {
        if(inLoop == false)
        {
            Lox.error(stmt.breakToken, "Can use 'break' only inside loop bodies.");
        }
        return null;
    }

    @Override
    public Void visitVariableExpr(Expr.Variable expr)
    {
        if(!scopes.empty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE)
        {
            Lox.error(expr.name, "Cannot read local variable in its own intializer.");
            return null;
        }

        resolveLocal(expr, expr.name);
        return null;
    }

    private void resolveLocal(Expr expr, Token name)
    {
        for (int i = scopes.size() - 1; i >= 0; i--)
        {
          if (scopes.get(i).containsKey(name.lexeme)) {
            interpreter.resolve(expr, scopes.size() - 1 - i);
            return;
          }
        }
    }

    @Override
    public Void visitThisExpr(Expr.This expr)
    {
        if(currClass == ClassType.NONE){
            Lox.error(expr.keyword, "Cannot use 'this' outside a class.");
            return null;    
        }
        if(currFuntion == FunctionType.STATICMETHOD)
        {
            Lox.error(expr.keyword, "Cannot use 'this' inside a static method.");
        }
        resolveLocal(expr, expr.keyword);
        return null;
    }

    @Override
    public Void visitBinaryExpr(Expr.Binary expr) {
      resolve(expr.left);
      resolve(expr.right);
      return null;
    }
    
    @Override
    public Void visitCallExpr(Expr.Call expr) {
      resolve(expr.calee);
  
      for (Expr argument : expr.arguments) {
        resolve(argument);
      }
  
      return null;
    }
  
    @Override
    public Void visitGroupingExpr(Expr.Grouping expr) {
      resolve(expr.expression);
      return null;
    }
    
    @Override
    public Void visitLiteralExpr(Expr.Literal expr) {
      return null;
    }

    @Override
    public Void visitLogicalExpr(Expr.Logical expr) {
      resolve(expr.left);
      resolve(expr.right);
      return null;
    }

    @Override
    public Void visitUnaryExpr(Expr.Unary expr) {
      resolve(expr.right);
      return null;
    }
  
    @Override
    public Void visitTernaryExpr(Expr.Ternary expr)
    {
        resolve(expr.Condition);
        resolve(expr.TrueBranch);
        resolve(expr.FalseBranch);
        return null;
    }

    @Override
    public Void visitAnonymousFunctionExpr(Expr.AnonymousFunction expr)
    {
        FunctionType prev = currFuntion;
        currFuntion = FunctionType.ANONYMOUSFUNCTION;
        resolveFunction(expr);
        currFuntion = prev;
        return null;
    }

    @Override
    public Void visitGetExpr(Expr.Get expr) {
      resolve(expr.object);
      return null;
    }  

    @Override
    public Void visitSetExpr(Expr.Set expr)
    {
        resolve(expr.object);
        resolve(expr.value);
        return null;
    }

    @Override
    public Void visitSuperExpr(Expr.Super expr)
    {
        if(currClass == ClassType.NONE)
        {
            Lox.error(expr.keyword,"Cannot use 'super' outside a class");
        }
        else if(currClass != ClassType.SUBCLASS)
        {
            Lox.error(expr.keyword, "Cannot use 'super' if class does not have a superclass.");
        }
        else if(currFuntion == FunctionType.STATICMETHOD)
        {
            Lox.error(expr.keyword, "Cannot use 'super' in a static method."); //We stick to java style where super cannot be used in a static context.
        }
        resolveLocal(expr, expr.keyword);
        return null;
    }

    private void resolveFunction(Expr.AnonymousFunction function) {
        beginScope();
        for (Token param : function.parameters) {
          declare(param);
          define(param);
        }
        resolve(function.body);
        endScope();
    }    
}
