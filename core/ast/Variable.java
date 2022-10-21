package core.ast;

import core.Token;

public class Variable extends Expr {
    Variable(Token name) {
        this.name = name;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }

    public final Token name;
}
