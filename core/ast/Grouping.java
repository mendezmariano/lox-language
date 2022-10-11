package core.ast;

public class Grouping extends Expr {
    public Grouping(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }

    public final Expr expression;
}
