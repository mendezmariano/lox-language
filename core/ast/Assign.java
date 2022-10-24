package core.ast;

import core.Token;

public class Assign extends Expr {
    public Assign(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitAssignExpr(this);
    }

    public final Token name;
    public final Expr value;
}
