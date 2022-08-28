import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    
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
           
        }
    }

    // el núcleo del interprete 
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        // For now, just print the tokens.
        for (Token token : tokens) {
            System.out.println(token);
           }
    }
    


}
