package core.ast;
import java.util.List;

public  abstract class Expr {

abstract <R> R accept(Visitor<R> visitor);
}
