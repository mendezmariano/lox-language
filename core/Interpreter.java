package core;

import java.util.List;
import core.ast.*;
import core.TokenType;

public class Interpreter implements Visitor<Object> ,
                                    StmtVisitor<Void>{

    // EL AMBIENTE 
    private Environment environment = new Environment();

    // toma el AST y lo evalua
    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
            execute(statement);
        }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);    
    }

    // cuando visita un bloque lo ejecuta
    @Override
    public Void visitBlockStmt(Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
    return null;
    }

    // ejecuta la semantica del bloque
    // crea un ambito para las variables locales
    // ejecuta los stmts
    // devuelve el ambito padre 
    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }


    // Convierte un  valor en un string
    private String stringify(Object object) {
        
        if (object == null) return "nil";
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
            text = text.substring(0, text.length() - 2);
        }
            return text;
        }
        return object.toString();
    }



    // evaluar un literal
    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    //Evaluar un paréntesis
    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    // evaluar una expresión unaria
    // primero se evalúa la parte derecha y luego 
    // la parte del operador unario
    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);
    
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
        
            case BANG:
            return !isTruthy(right);
        }
            // Unreachable.
        return null;
    }


    @Override
    public Object visitVariableExpr(Variable expr) {
        return environment.get(expr.name);
    }


    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
         throw new RuntimeError(operator, "Operand must be a number.");
     }
     
     private void checkNumberOperands(Token operator,Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    // evaluar expressions stmt
    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    // Se evalua la expresion que recibe el print
    // posteriormente se imprime

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }


    // Al visitar un var stmt se define la variable
    // si fue inicializada se la inicializa sino 
    // su valor es null
    @Override
    public Void visitVarStmt(Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    // Cuando se visita una Asignacion se evalua 
    // ,posteriormete se agrega al ambiente
    // y se devuelve el valor evaluado 
    @Override
    public Object visitAssignExpr(Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }

    // evaluar expresiones binarias
    @Override
    public Object visitBinaryExpr(Binary expr) {

        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case GREATER:
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
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expr.operator,"Operands must be two numbers or two strings.");

            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;

            case BANG_EQUAL: return !isEqual(left, right);
            case EQUAL_EQUAL: return isEqual(left, right);
        }
        // Unreachable.
        return null;
    }

    

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    
    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }


}
