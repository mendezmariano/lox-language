package core.ast;


public abstract class Stmt {

public abstract <R> R accept(Visitor<R> visitor);
}
