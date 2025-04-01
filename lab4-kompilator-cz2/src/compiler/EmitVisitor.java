package compiler;

import SymbolTable.GlobalSymbols;
import interpreter.VariableHolder;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import grammar.firstBaseVisitor;
import grammar.firstParser;

import java.util.List;
import java.util.Map;

public class EmitVisitor extends firstBaseVisitor<ST> {
    private final STGroup stGroup;

    private static int ifLabelCounter = 1;
    private static int cmpLabelCounter = 1;
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
        if (node.getText().equals("<EOF>")) {
            return null;
        }
        return new ST("Terminal node:<n>").add("n", node.getText());
    }

    @Override
    public ST visitInt_tok(firstParser.Int_tokContext ctx) {
        ST st = stGroup.getInstanceOf("int");
        st.add("i", ctx.INT().getText());
        return st;
    }

    @Override
    public ST visitLogic_int_tok(firstParser.Logic_int_tokContext ctx) {
        return stGroup.getInstanceOf("int").add("i", ctx.INT().getText());
    }

    // operacje arytmetyczne
    @Override
    public ST visitBinOp(firstParser.BinOpContext ctx) {
        Map<Integer, String> templateMap = Map.of(
                firstParser.MUL, "mnoz",
                firstParser.DIV, "dziel",
                firstParser.ADD, "dodaj",
                firstParser.SUB, "odejmij"
        );

        String templateName = templateMap.get(ctx.op.getType());
        if (templateName == null) {
            throw new IllegalArgumentException("Nieznana operacja: " + ctx.op.getText());
        }

        ST st = stGroup.getInstanceOf(templateName);
        st.add("p1", visit(ctx.l)).add("p2", visit(ctx.r));
        return st;
    }

    // nawiasy
    @Override
    public ST visitPars(firstParser.ParsContext ctx) {
        return visit(ctx.expr());
    }

    // zmienne - deklaracja, inicjalizacja, przypisanie, wyłuskanie
    @Override
    public ST visitVar_def(firstParser.Var_defContext ctx) {
        ST st;
        if (ctx.varVal != null) {
            // inicjalizacja zmiennej
            ST result = visit(ctx.varVal);
            st = stGroup.getInstanceOf("inicjalizuj");
            st.add("nazwa", ctx.varName.getText()).add("wartosc", result);
        } else {
            // deklaracja zmiennej
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

        st.add("nazwa", varName).add("wartosc", visit);
        return st;
    }

    @Override
    public ST visitId_tok(firstParser.Id_tokContext ctx) {
        ST st = stGroup.getInstanceOf("wyluskaj");
        String variableName = ctx.ID().getText();
        return st.add("nazwa", variableName);
    }

    @Override
    public ST visitLogic_id_tok(firstParser.Logic_id_tokContext ctx) {
        return stGroup.getInstanceOf("wyluskaj").add("nazwa", ctx.ID().getText());
    }

    // if stat
    @Override
    public ST visitIf_stat(firstParser.If_statContext ctx) {
        ST ifStat = stGroup.getInstanceOf("if_stat");
        ifStat.add("cond", visit(ctx.cond))
                .add("then", visit(ctx.then))
                .add("if_label_counter", ifLabelCounter++);

        if (ctx.else_ != null) {
            ifStat.add("else_then", visit(ctx.else_));
        }

        return ifStat;
    }

    // compare statements
    @Override
    public ST visitLogic_comp(firstParser.Logic_compContext ctx) {
        String operation;
        switch (ctx.op.getType()) {
            case firstParser.GREAT -> operation = "comp_great";
            case firstParser.GREATEQ -> operation = "comp_great_eq";
            case firstParser.LESS -> operation = "comp_less_eq";
            case firstParser.LESSEQ -> operation = "comp_less";
            case firstParser.EQ -> operation = "comp_eq";
            case firstParser.NEQ -> operation = "comp_neq";

            default -> throw new IllegalArgumentException("Nieznana operacja logiczna: " + ctx.op.getText());
        }

        ST st = stGroup.getInstanceOf("comp_operation");
        st.add("p1", visit(ctx.l))
                .add("p2", visit(ctx.r))
                .add("compNumber", cmpLabelCounter++)
                .add("operationTemplate", operation);

        return st;
    }

    // TODO: parametry funkcji
    @Override
    public ST visitFunc_def(firstParser.Func_defContext ctx) {
        // ...zaznacz, że funckja i załóż tablice symboli
        // ctx.par jest listą parametrów

        ST st = stGroup.getInstanceOf("func_def")
                .add("name", ctx.name.getText())
                .add("body", visit(ctx.func_body));

        List<Token> params = ctx.par;
        st.add("pars", params);

        return st;
    }

    // function call
    @Override
    public ST visitFunc_call(firstParser.Func_callContext ctx) {
        ST st = stGroup.getInstanceOf("func_call");
        st.add("name", ctx.name.getText());

        List<firstParser.ExprContext> args = ctx.arg;
        for (firstParser.ExprContext arg : args) {
            ST visit = visit(arg);
            st.add("pars", visit);
        }

        return st;
    }

    // other
    @Override
    public ST visitBlock_real(firstParser.Block_realContext ctx) {
        ST deflt = stGroup.getInstanceOf("deflt");

        List<firstParser.BlockContext> blocks = ctx.block();
        for (firstParser.BlockContext block : blocks) {
            ST st = visit(block);
            if (st != null) {
                deflt.add("elem", st);
            }

        }
        return deflt;
    }

}
