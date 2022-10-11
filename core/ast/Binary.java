package core.ast;

import core.Token;

public class Binary extends Expr {
    public Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitBinaryExpr(this);
    }

    public final Expr left;
    public final Token operator;
    public final Expr right;
}
