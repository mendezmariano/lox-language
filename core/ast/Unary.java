package core.ast;

import core.Token;

public class Unary extends Expr {
    public Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }

    public final Token operator;
    public final Expr right;
}
