package core;

import java.util.List;
import static core.TokenType.*;
import core.ast.Expr;
import core.ast.Binary;
import core.ast.Grouping;
import core.ast.Unary;
import core.ast.Literal;

class Parser{

    private final List<Token> tokens;
    private int current=0;

    Parser(List<Token> tokens){
        this.tokens= tokens;
    }

    // primera regla de la gramatica 
    private Expr expression(){
        return equality();
    }

    // equality → comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr expr = comparison();
        
        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    //otra regla
    //comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;

    private Expr comparison(){
        Expr expr = term();
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }
    // term→ factor ( ( "-" | "+" ) factor )* ;
    private Expr term() {
        Expr expr = factor();
        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }

    // factor→ unary ( ( "/" | "*" ) unary )* ;
    private Expr factor() {
        Expr expr = unary();
        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Binary(expr, operator, right);
        }
        return expr;
    }



    //unary→ ( "!" | "-" ) unary;
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Unary(operator, right);
        }
        return primary();
    }
    //primary→ NUMBER | STRING | "true" | "false" | "nil"
    //       | primary ;
    //       | "(" expression ")" ;

    private Expr primary() {
      
        if (match(FALSE)) return new Literal(false);
        if (match(TRUE)) return new Literal(true);
        if (match(NIL)) return new Literal(null);
        
        if (match(NUMBER, STRING)) {
            return new Literal(previous().literal);
        }
        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expr);
        }
    }

    /* 
        Parser Infrastrucuture
        ******************************************************
    */

    /* match(): Verifica si el token actual tiene alguno de los tipos dados. Si es así,
    consume el token y devuelve true . De lo contrario, devuelve falso y se va.
    el token actual solo. El método match() se define en términos de dos más
    operaciones fundamentales.
    */

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }


    /* check():El método check() devuelve verdadero si el token actual es del tipo dado.
    A diferencia de match(), nunca consume el token, solo lo mira.
    */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    /* advance(): El método advance() consume el token actual y lo devuelve, similar a
    cómo el método correspondiente de nuestro escáner se rastreó a través de los caracteres
    */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }


    /*isAtEnd(): comprueba si nos hemos quedado sin tokens para analizar. 
     */

    private boolean isAtEnd() {
        return peek().type == EOF;
        
    }

    /*peek(): devuelve el token actual que aún tenemos que consumir */
    private Token peek() {
        return tokens.get(current);
    }
    
    /*anterior(): devuelve el más token consumido recientemente. 
    Este último hace que sea más fácil usar match() y luego
    acceder al token recién emparejado. */
    
    private Token previous() {
        return tokens.get(current - 1);
    }

}