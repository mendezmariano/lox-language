package core;

import core.ast.*;

public class Interpreter implements Visitor<Object> {

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
        }
        // Unreachable.
        return null;
    }



    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
}
