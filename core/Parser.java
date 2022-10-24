package core;

import java.util.ArrayList;
import java.util.List;
import static core.TokenType.*;
import core.ast.Expr;
import core.ast.Binary;
import core.ast.Grouping;
import core.ast.Unary;
import core.ast.Literal;
import core.ast.Stmt;
import core.ast.Print;
import core.ast.Expression;
import core.ast.Var;
import core.ast.Variable;

public class Parser{

    private static class ParseError extends RuntimeException {}
    /*
     * Esta es una clase centinela simple que usamos para desenredar el analizador. 
     * El método error() devuelve el error en lugar de lanzarlo porque queremos dejar 
     * que el método de llamada dentro del analizador decida si se desenreda (unwind) o no.
     */
    private final List<Token> tokens;
    private int current=0;

    Parser(List<Token> tokens){
        this.tokens= tokens;
    }

    // determina el inicio desde donde se comienza a parsear
    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {
            statements.add(declaration());
        }
        return statements;
    }

    
    private Expr expression(){
        return equality();
    }


    // Parseo de los Statements
    // statement→ exprStmt| printStmt ;
    private Stmt statement() {
        if (match(PRINT)) return printStatement();
        
        return expressionStatement();
    }

    // Print Statement
    //printStmt→ "print" expression ";" ;

    private Stmt printStatement() {
 
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Print(value);
    }

    // varDecl→ "var" IDENTIFIER ( "=" expression )? ";" ;

    private Stmt varDeclaration() {
        Token name = consume(IDENTIFIER, "Expect variable name.");
        
        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Var(name, initializer);
    }

    // maneja las expression statements
    // exprStmt→ expression ";" ;
    private Stmt expressionStatement() {
        
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Expression(expr);
    }


    // declaracion stmt
    //declaration→ varDecl| statement ;
    private Stmt declaration() {
        try {
            if (match(VAR)) return varDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
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

        if (match(IDENTIFIER)) {
            return new Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expr);
        }
        
        throw error(peek(), "Expect expression.");
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

    /* Parser errors */
    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }


    /* Descarta tokens hasta que cree que ha encontrado un límite de declaración. 
    Después de detectar un ParseError, lo llamaremos y, con suerte, volveremos a estar sincronizados.
    Cuando funciona bien, hemos descartado tokens que probablemente habrían 
    causado errores en cascada de todos modos, y ahora podemos analizar el resto del archivo
     a partir de la siguiente declaración. */


    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON) return;
            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                return;
            }
            advance();
        }
    }


}