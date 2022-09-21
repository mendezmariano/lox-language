
package core;

import java.util.List;
import core.TokenType.*;


class Parser{

    private final List<Token> tokens;
    private int current=0;

    Parser(List<Token> tokens){
        this.tokens= tokens;
    }

    // primera regla de la gramatica 
    private Expr expression(){
        return equalty();
    }
}