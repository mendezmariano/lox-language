
package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static core.TokenType.*;

public class Scanner {
    private final String source;                                    // stream de caracteres
    private final List<Token> tokens= new ArrayList<>();            // stream de tokens


    Scanner(String source) {
        this.source = source;
    }


}
