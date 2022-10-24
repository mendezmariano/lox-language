package core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import core.ast.Expr;
import core.ast.Stmt;

public class Lox {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;  // manejo de errores
    static boolean hadRuntimeError = false; // manejo de Runtime errors



    public static void main(String[] args) throws Exception {
        if (args.length>1){
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length==1){
            runFile(args[0]);      
        } else {
            runPrompt();
        }
    }

    // ejecuta un archivo --> un script
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        // indica el error code cuando sale 
        if (hadError) System.exit(65);
        if (hadRuntimeError) System.exit(70);
    }
    
    // ejecuta el interprete  --> REPL
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false; // se inicializa el handler de errores 
        }
    }

    // el núcleo del interprete 
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);

        List<Stmt> statements = parser.parse();
        
        // Stop if there was a syntax error.
        if (hadError) return;
        interpreter.interpret(statements);
        //System.out.println(new AstPrinter().print(expression));

    }

    static void error(int line, String message) {
        report(line, "", message);
    
    }

    static void runtimeError(RuntimeError error) {
        
        System.err.println(error.getMessage() +
        "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    private static void report(int line, String where,String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    /* muestra el error encontrado */

    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, " at end", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

}
