package core.ast;
import java.util.List;

public  abstract class Expr {

public abstract <R> R accept(Visitor<R> visitor);
}
