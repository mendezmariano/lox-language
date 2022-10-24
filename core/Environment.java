package core;


import java.util.HashMap;
import java.util.Map;



public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
    }

    // operacion que define un binding 
    // entre variable y valor
    
    // eleccion semantica
    
    // var a = "before";
    // print a; // "before".
    // var a = "after";
    // print a; // "after".

    // No solo define una nueva variable 
    // sino que redefine una existente!

    void define(String name, Object value) {
        values.put(name, value);
    }


}
