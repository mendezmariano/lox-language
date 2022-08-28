
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



    private boolean isAtEnd(){          // termino?
        return current >=source.length();
    }


}
