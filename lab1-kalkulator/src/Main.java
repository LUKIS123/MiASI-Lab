import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // create a CharStream from input
        CharStream input = CharStreams.fromString(
                "2*(6+2);"
        );

        // create a lexer that feeds off of input CharStream
        ExprLexer lexer = new ExprLexer(input);

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // create a parser that feeds off the tokens buffer
        ExprParser parser = new ExprParser(tokens);

        // start parsing at the program rule
        ParseTree tree = parser.program();
        // System.out.println(tree.toStringTree());

        // create a visitor to traverse the parse tree
        BasicCalculatorVisitor visitor = new BasicCalculatorVisitor();
        Integer result = visitor.visit(tree);
        System.out.println("Result: " + result);
    }
}


// za tydzien interpreter - zmienne, przypisanie,
// funckje tez? // petle tez?
// startujemy z kodu z repo
