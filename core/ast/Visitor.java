package core.ast;



public interface Visitor<T> {
public T visitBinaryExpr(Binary expr);
public T visitGroupingExpr(Grouping expr);
public T visitLiteralExpr(Literal expr);
public T visitUnaryExpr(Unary expr);
//public T visitVariableExpr(Variable expr);
public T visitExpressionStmt(Expression stmt);
}
