
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

    private static final Map<String, TokenType> keywords;

    static {
    keywords = new HashMap<>();
    keywords.put("and",AND);
    keywords.put("class",CLASS);
    keywords.put("else",ELSE);
    keywords.put("false",FALSE);
    keywords.put("for",FOR);
    keywords.put("fun",FUN);
    keywords.put("if",IF);
    keywords.put("nil",NIL);
    keywords.put("or",OR);
    keywords.put("print",PRINT);
    keywords.put("return", RETURN);
    keywords.put("super",SUPER);
    keywords.put("this",THIS);
    keywords.put("true",TRUE);
    keywords.put("var",VAR);
    keywords.put("while",WHILE);
    }

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
          
            //lexemas de dos caracteres 
            //operadores ==,!= y otros
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // reconocimientos de tokens mas complejos
            // manejo de lexemas mas complejos
            case '/':
                if (match('/')) {
                // Es un comentario ir al final de la linea.
                while (peek() != '\n' && !isAtEnd()) 
                        advance();
                } else {
                    addToken(SLASH);
                }
                break;

            // manejo de espacios
            case ' ':
            case '\r':
            case '\t':
                // Ignorar espacios en blanco
                break;
            case '\n':
                line++;
                break;

            // literales 
            case '"': string(); break;
            // manejo de errores
            default:
                if (isDigit(c)) {
                    number();
                }else if (isAlpha(c)) {
                    identifier(); 
                } else {
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = IDENTIFIER;
        addToken(type);
    }
        

    private void number() {
        while (isDigit(peek())) advance();
        // busca y avanza hasta el .
        if (peek() == '.' && isDigit(peekNext())) {
        // Consume el "."
        advance();
        // sigue con la parte decimal
        while (isDigit(peek())) advance();
        }
    addToken(NUMBER,
    Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }
        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // Las " que cierran el literal.
        advance();
        
        // sacar las comillas dobles.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }
    
    // lookahead
    //  no consume el próximo caracter solo
    //  se fija hacia adelante
    //  cuantos menos hay mas rápido es el scanner
    // la cantidad depende de la gramática 
     
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    // lookahead el proximo del corriente
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        
        current++;
        return true;
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

    // función utilidad que verifica 
    // que un caracter sea un dígito
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
        (c >= 'A' && c <= 'Z') ||
        c == '_';
    }
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);	
    }

}
