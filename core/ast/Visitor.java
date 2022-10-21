package core.ast;



public interface Visitor<T> {
T visitBinaryExpr(Binary expr);
T visitGroupingExpr(Grouping expr);
T visitLiteralExpr(Literal expr);
T visitUnaryExpr(Unary expr);
T visitVariableExpr(Variable expr);
T visitExpressionStmt(Expression stmt);
T visitPrintStmt(Print stmt);
T visitVarStmt(Var stmt);
}
