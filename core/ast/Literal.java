package core.ast;

public class Literal extends Expr {
    Literal(Object value) {
        this.value = value;
    }

    @Override
    <T> T accept(Visitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }

    final Object value;
}
