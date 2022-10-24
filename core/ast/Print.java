package core.ast;


public class Print extends Stmt {
    
    public Print(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }

    public final Expr expression;
}
