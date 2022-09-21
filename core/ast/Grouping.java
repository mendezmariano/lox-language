package core.ast;

public class Grouping extends Expr {
    Grouping(Expr expression) {
        this.expression = expression;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }

    final Expr expression;
}
