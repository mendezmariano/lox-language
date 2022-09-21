package core.ast;

import core.Token;

public class Binary extends Expr {
    Binary(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visitBinaryExpr(this);
    }

    final Expr left;
    final Token operator;
    final Expr right;
}
