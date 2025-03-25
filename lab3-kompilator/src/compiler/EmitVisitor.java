package compiler;

import SymbolTable.GlobalSymbols;
import interpreter.VariableHolder;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import grammar.firstBaseVisitor;
import grammar.firstParser;

import java.util.Map;

public class EmitVisitor extends firstBaseVisitor<ST> {
    private final STGroup stGroup;

    private final GlobalSymbols<VariableHolder<Integer>> globalVariableMemory = new GlobalSymbols<>(); // Przechowywanie zmiennych

    public EmitVisitor(STGroup group) {
        super();
        this.stGroup = group;
    }

    @Override
    protected ST defaultResult() {
        return stGroup.getInstanceOf("deflt");
    }

    @Override
    protected ST aggregateResult(ST aggregate, ST nextResult) {
        if (nextResult != null)
            aggregate.add("elem", nextResult);
        return aggregate;
    }


    @Override
    public ST visitTerminal(TerminalNode node) {
        return new ST("Terminal node:<n>").add("n", node.getText());
    }

    @Override
    public ST visitInt_tok(firstParser.Int_tokContext ctx) {
        ST st = stGroup.getInstanceOf("int");
        st.add("i", ctx.INT().getText());
        return st;
    }

    // operacje arytmetyczne
    @Override
    public ST visitBinOp(firstParser.BinOpContext ctx) {
        // ST st = stGroup.getInstanceOf("dodaj");
        // return st.add("p1",visit(ctx.l)).add("p2",visit(ctx.r));

        Map<Integer, String> templateMap = Map.of(
                firstParser.ADD, "dodaj",
                firstParser.SUB, "odejmij",
                firstParser.MUL, "mnoz",
                firstParser.DIV, "dziel"
        );

        String templateName = templateMap.get(ctx.op.getType());
        if (templateName == null) {
            throw new IllegalArgumentException("Nieznana operacja: " + ctx.op.getText());
        }

        ST st = stGroup.getInstanceOf(templateName);
        st.add("p1", visit(ctx.l));
        st.add("p2", visit(ctx.r));
        return st;
    }

    // zmienne - deklaracja, inicjalizacja, przypisanie, wyłuskanie
    @Override
    public ST visitVar_def(firstParser.Var_defContext ctx) {
        ST st;
        if (ctx.varVal != null) { // inicjalizacja zmiennej
            ST result = visit(ctx.varVal);
            Integer val = null;

            if (result != null) {
                try {
                    String render = result.render();
                    val = Integer.valueOf(render);
                } catch (NumberFormatException ignored) {
                }
            }

            st = stGroup.getInstanceOf("inicjalizuj");
            st.add("nazwa", ctx.varName.getText()).add("wartosc", (val != null) ? val : result); // Jeśli nie liczba, używamy ST
        } else { // deklaracja zmiennej
            st = stGroup.getInstanceOf("dek");
            st.add("n", ctx.varName.getText());
        }

        return st;
    }

    @Override
    public ST visitAssign(firstParser.AssignContext ctx) {
        ST st = stGroup.getInstanceOf("przypisz");

        String varName = ctx.ID().getText(); // Pobieramy nazwę zmiennej

        ST visit = visit(ctx.expr());// Obliczamy wartość wyrażenia
        String render = visit.render();

        st.add("nazwa", varName).add("wartosc", render);
        return st;
    }

    @Override
    public ST visitId_tok(firstParser.Id_tokContext ctx) {
        ST st = stGroup.getInstanceOf("wyluskaj");
        String variableName = ctx.ID().getText();
        return st.add("nazwa", variableName);
    }
}


//    // todo: drukowanie
//    @Override
//    public ST visitPrint_stat(firstParser.Print_statContext ctx) {

/// /        var st = ctx.expr();
/// /        var result = visit(st);
/// /        Map<String, Object> attributes = result.getAttributes();
/// /
/// /        System.out.printf("|%s=%s|\n", "tokStream.getText(st)", result); //drukuje spacje z ukrytego kanału, ale nie ->skip
/// /        return result;
//
//        var st = ctx.expr();
//        ST result = visit(st);
//
//        String exprText = ctx.getText(); // Pobiera tekst wyrażenia (bez spacji pominiętych przez lexer)
//        if (result != null) { // Jeśli wynik to ST, wypisujemy jego treść
//            System.out.printf("|%s=%s|\n", exprText, result.render());
//        } else { // Jeśli wynik to coś innego, po prostu wyświetlamy go
//            System.out.printf("|%s=%s|\n", exprText, result);
//        }
//
//        return result;
//    }