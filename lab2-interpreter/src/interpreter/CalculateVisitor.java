package interpreter;

import SymbolTable.GlobalSymbols;
import grammar.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Interval;

public class CalculateVisitor extends firstBaseVisitor<Integer> {
    private TokenStream tokStream = null;
    private CharStream input = null;

    private static final int ERROR = -2147483647;
    private final GlobalSymbols<VariableHolder<Integer>> globalVariableMemory = new GlobalSymbols<>(); // Przechowywanie zmiennych

    @Override
    public Integer visitExpr_stat(firstParser.Expr_statContext ctx) {
        if (ctx.expr() != null) {
            return visit(ctx.expr());
        } else {
            return ERROR;
        }
    }

    @Override
    public Integer visitAssign(firstParser.AssignContext ctx) {
        String varName = ctx.ID().getText(); // Pobieramy nazwę zmiennej
        int value = visit(ctx.expr());       // Obliczamy wartość wyrażenia

        if (globalVariableMemory.hasSymbol(varName)) {
            globalVariableMemory.getSymbol(varName).Value = value; // Aktualizujemy wartość
            return value;
        }

        // TODO: jesli nie ma zmiennej zadeklarowanej to rzuca wyjatek Variable y does not exist!
        //        globalVariableMemory.newSymbol(varName, new VariableHolder<>(varName, value));    // Zapisujemy do pamięci
        return value;
    }

    @Override
    public Integer visitId_tok(firstParser.Id_tokContext ctx) {
        String varName = ctx.ID().getText();
        return globalVariableMemory.getSymbol(varName).Value;
    }

    @Override
    public Integer visitVar_def(firstParser.Var_defContext ctx) {
        String variableName = ctx.varName.getText();
        Integer variableValue = null;

        if (ctx.varVal != null) {
            try {
                variableValue = visit(ctx.varVal); // Obliczamy wartość wyrażenia
            } catch (Exception ignored) {
            }
        }

        globalVariableMemory.newSymbol(variableName, new VariableHolder<>(variableName, variableValue));
        return variableValue;
    }

    // logika
    @Override
    public Integer visitArOp(firstParser.ArOpContext ctx) {
        int result = 0;
        switch (ctx.op.getType()) {
            case firstLexer.GREAT -> result = visit(ctx.l) > visit(ctx.r) ? 1 : 0;
            case firstLexer.GREATEQ -> result = visit(ctx.l) >= visit(ctx.r) ? 1 : 0;
            case firstLexer.LESS -> result = visit(ctx.l) < visit(ctx.r) ? 1 : 0;
            case firstLexer.LESSEQ -> result = visit(ctx.l) <= visit(ctx.r) ? 1 : 0;
            case firstLexer.EQ -> result = visit(ctx.l).equals(visit(ctx.r)) ? 1 : 0;
            case firstLexer.NEQ -> result = !visit(ctx.l).equals(visit(ctx.r)) ? 1 : 0;
            case firstLexer.AND -> result = visit(ctx.l) != 0 && visit(ctx.r) != 0 ? 1 : 0;
            case firstLexer.OR -> result = visit(ctx.l) != 0 || visit(ctx.r) != 0 ? 1 : 0;
        }

        return result;
    }

    @Override
    public Integer visitUnOp(firstParser.UnOpContext ctx) {
        Integer result = visit(ctx.r);
        if (result == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    // petla
    @Override
    public Integer visitWhileLoop(firstParser.WhileLoopContext ctx) {
        Integer result = 0;
        while (visit(ctx.cond) != 0) {
            result = visit(ctx.loopBody);
        }
        return result;
    }

    public CalculateVisitor(CharStream inp) {
        super();
        this.input = inp;
    }

    public CalculateVisitor(TokenStream tok) {
        super();
        this.tokStream = tok;
    }

    public CalculateVisitor(CharStream inp, TokenStream tok) {
        super();
        this.input = inp;
        this.tokStream = tok;
    }

    private String getText(ParserRuleContext ctx) {
        int a = ctx.start.getStartIndex();
        int b = ctx.stop.getStopIndex();
        if (input == null) throw new RuntimeException("Input stream undefined");
        return input.getText(new Interval(a, b));
    }

    @Override
    public Integer visitIf_stat(firstParser.If_statContext ctx) {
        Integer result = 0;
        if (visit(ctx.cond) != 0) {
            result = visit(ctx.then);
        } else {
            if (ctx.else_ != null)
                result = visit(ctx.else_);
        }
        return result;
    }

    @Override
    public Integer visitPrint_stat(firstParser.Print_statContext ctx) {
        var st = ctx.expr();
        var result = visit(st);
//        System.out.printf("|%s=%d|\n", st.getText(), result); //nie drukuje ukrytych ani pominiętych spacji
//        System.out.printf("|%s=%d|\n", getText(st), result); //drukuje wszystkie spacje
        System.out.printf("|%s=%d|\n", tokStream.getText(st), result); //drukuje spacje z ukrytego kanału, ale nie ->skip
        return result;
    }

    @Override
    public Integer visitInt_tok(firstParser.Int_tokContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitPars(firstParser.ParsContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Integer visitBinOp(firstParser.BinOpContext ctx) {
        Integer result = 0;
        switch (ctx.op.getType()) {
            case firstLexer.ADD:
                result = visit(ctx.l) + visit(ctx.r);
                break;
            case firstLexer.SUB:
                result = visit(ctx.l) - visit(ctx.r);
                break;
            case firstLexer.MUL:
                result = visit(ctx.l) * visit(ctx.r);
                break;
            case firstLexer.DIV:
                try {
                    result = visit(ctx.l) / visit(ctx.r);
                } catch (Exception e) {
                    System.err.println("Div by zero");
                    throw new ArithmeticException();
                }
        }
        return result;
    }

}
