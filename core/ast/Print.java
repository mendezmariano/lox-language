package core.ast;


public class Print extends Stmt {
    Print(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }

    public final Expr expression;
}
