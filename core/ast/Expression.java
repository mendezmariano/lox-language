package core.ast;



public class Expression extends Stmt {
    Expression(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }

    public final Expr expression;
}
