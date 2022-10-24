package core;


import java.util.HashMap;
import java.util.Map;



public class Environment {
    final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    // constructor del global
    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        // se crea una cadena de busqueda 
        // si la variable no esta en el scope
        // se busca en el que lo engloba
        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name,"Undefined variable '" + name.lexeme + "'.");
    }

    // asignacion
    // ademas si no esta la variable en el scope 
    // se lo agregA
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
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
