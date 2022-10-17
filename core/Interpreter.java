package core;

import core.ast.*;

public class Interpreter implements Visitor<Object> {

    // toma el AST y lo evalua
    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
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
            
                return -(double)right;
        
            case BANG:
            return !isTruthy(right);
        }
            // Unreachable.
        return null;
    }

    
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    // evaluar expresiones binarias
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {

        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case GREATER:
                return (double)left > (double)right;

            case GREATER_EQUAL:
                return (double)left >= (double)right;
            case LESS:
                return (double)left < (double)right;
            case LESS_EQUAL:
                return (double)left <= (double)right;
            case MINUS:
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                break;

            case SLASH:
                return (double)left / (double)right;
            case STAR:
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
