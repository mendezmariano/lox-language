package core.ast;

import core.Token;

public class Var extends Stmt {
    Var(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitVarStmt(this);
    }

    public final Token name;
    public final Expr initializer;
}
