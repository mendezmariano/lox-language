package core;

import core.ast.*;

public class AstPrinter implements Visitor<String> {

    String print (Expr expr){
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return binary( expr.left, expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize( expr.expression);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }
    
    @Override
    public String visitUnaryExpr(Unary expr) {
    return parenthesize( expr.right);
    }


    private String parenthesize( Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Expr expr : exprs) {
            //builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }


    private String binary(Expr left, String operator , Expr right) {
        StringBuilder builder = new StringBuilder();
        
        //builder.append(" ");
        builder.append(left.accept(this));
        builder.append(" ");
        builder.append(operator);
        builder.append(" ");
        builder.append(right.accept(this));
        
        return builder.toString();
    }

    
}
