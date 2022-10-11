package core.ast;

import core.ast.Unary;

public interface Visitor<T> {
T visitBinaryExpr(Binary expr);
T visitGroupingExpr(Grouping expr);
T visitLiteralExpr(Literal expr);
T visitUnaryExpr(Unary expr);
}
