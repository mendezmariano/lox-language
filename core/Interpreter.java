package core;

import core.ast.*;

public class Interpreter implements Visitor<Object> {


    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    
}
