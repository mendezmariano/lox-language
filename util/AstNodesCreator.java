package util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;


public class AstNodesCreator{

  // Formato para generar las clases que seran los
  // nodos del AST
  public static List<String> AstNodesDefinitionlist = Arrays.asList(
    "Binary: Expr left, Token operator, Expr right",
    "Grouping : Expr expression",
    "Literal: Object value",
    "Unary: Token operator, Expr right"
    );

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];
        defineAst(outputDir, "Expr",AstNodesDefinitionlist);
    }



    private static void defineAst(
        String outputDir, 
        String baseName, 
        List<String> types) throws IOException{

        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        writer.println();
        // método accept de la clase base
        writer.println("abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.close();
        
        // Las Clases del AST
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            path = outputDir + "/" + className + ".java";
            defineType(path, baseName, className, fields);
        }
        // La definicion de la interface del visitor
        path = outputDir + "/" +  "Visitor.java";
        defineVisitor(path, baseName, types);
    }

    private static void defineType (
        String path, 
        String baseName,
        String className, 
        String fieldList)throws IOException {

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("static class " + className + " extends " +
    baseName + " {");
        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");
        
        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("        this." + name + " = " + name + ";");
        }
        writer.println("    }");
        // Patron Visitor 
        writer.println();
        writer.println("    @Override");
        writer.println("    <T> T accept(Visitor<T> visitor) {");
        writer.println("        return visitor.visit" +className + baseName + "(this);");
        writer.println("    }");

        // Atributos.
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }
        writer.println("}");
        writer.close();
    }

    private static void defineVisitor(
        String path, 
        String baseName, 
        List<String> types)throws IOException {

        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("interface Visitor<T> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    T visit" + typeName + baseName + "(" +
            typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println("}");
        writer.close();
    }

}