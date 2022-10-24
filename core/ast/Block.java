package core.ast;

import java.util.List;
import core.Token;

public  class Block extends Stmt {
    public Block(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitBlockStmt(this);
    }

    public final List<Stmt> statements;
}
