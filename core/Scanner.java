
package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static core.TokenType.*;

public class Scanner {
    private final String source;                                    // stream de caracteres
    private final List<Token> tokens= new ArrayList<>();            // stream de tokens
    private int start=0;        // inicio de la linea
    private int current=0;      // posicion caracter actual
    private int line=1;         // linea escaneada


    Scanner(String source) {
        this.source = source;
    }

    // El corazón del lexer
    // escanea caracter a caracter 
    
    List<Token> scanTokens() {
        while (!isAtEnd()) {
        // el inicio del proximo lexema .
        start = current;
        scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }       

    private void scanToken() {
        char c = advance();
        switch (c) {
            //lexemas de un solo caracter
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
    
            // manejo de errores
            default:
            Lox.error(line, "Unexpected character.");
            break;
        }
    }

    private char advance() {            // nuevo char!
        return source.charAt(current++);
    }
        
    private void addToken(TokenType type) { // agrega un token
        addToken(type, null);
    }
    
    private void addToken(TokenType type, Object literal) { // agrega un token con texto
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }


    private boolean isAtEnd(){          // termino?
        return current >=source.length();
    }


}
