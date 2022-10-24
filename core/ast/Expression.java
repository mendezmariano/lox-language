package core.ast;



public class Expression extends Stmt {
    public Expression(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }

    public final Expr expression;
}
