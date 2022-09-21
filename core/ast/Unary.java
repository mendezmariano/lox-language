package core.ast;

import core.Token;

public class Unary extends Expr {
    Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }

    final Token operator;
    final Expr right;
}
