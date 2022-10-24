package  core.ast;

public interface StmtVisitor<T> {
    public T visitExpressionStmt(Expression stmt);
    public T visitPrintStmt(Print stmt);
    public T visitVarStmt(Var stmt);
  }