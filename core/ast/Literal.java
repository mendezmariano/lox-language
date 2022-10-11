package core.ast;

public class Literal extends Expr {
    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }

    public final Object value;
}
