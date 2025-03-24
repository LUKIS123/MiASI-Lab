public class BasicCalculatorVisitor extends ExprParserBaseVisitor<Integer> {
    private static final int ERROR = -2147483647;

    @Override
    public Integer visitProgram(ExprParser.ProgramContext ctx) {
        if (ctx.stat() != null) {
            return visit(ctx.stat());
        } else {
            return ERROR;
        }
    }

    @Override
    public Integer visitStat(ExprParser.StatContext ctx) {
        // return super.visitStat(ctx);

        if (ctx.expr() != null) {
            return visit(ctx.expr());
        } else {
            return ERROR;
        }
    }

    @Override
    public Integer visitExpr(ExprParser.ExprContext ctx) {

        // int values
        if (ctx.INT() != null) {
            return Integer.parseInt(ctx.INT().getText());
        }

        var left = ctx.getChild(0);
        var op = ctx.getChild(1).getText();
        var right = ctx.getChild(2);

        // nawias
        if (ctx.LPAREN() != null) {
            return visit(ctx.expr(0));
        }

        return switch (op) {
            case "*" -> visit(left) * visit(right);
            case "/" -> visit(left) / visit(right);
            case "+" -> visit(left) + visit(right);
            case "-" -> visit(left) - visit(right);
            default -> super.visitExpr(ctx);
        };


//        // multiply or divide
//        if (ctx.MUL() != null) {
//            return visit(ctx.expr(0)) * visit(ctx.expr(1));
//        }
//        if (ctx.DIV() != null) {
//            return visit(ctx.expr(0)) / visit(ctx.expr(1));
//        }
//
//        // add or subtract
//        if (ctx.ADD() != null) {
//            return visit(ctx.expr(0)) + visit(ctx.expr(1));
//        }
//        if (ctx.SUB() != null) {
//            return visit(ctx.expr(0)) - visit(ctx.expr(1));
//        }
//
//        // int values
//        if (ctx.INT() != null) {
//            return Integer.parseInt(ctx.INT().getText());
//        }
//
//        // default
//        return super.visitExpr(ctx);
    }

//    @Override
//    protected Integer aggregateResult(Integer aggregate, Integer nextResult) {
//        if (nextResult == null) {
//            return aggregate;
//        } else {
//            return aggregate + nextResult;
//        }
//    }
}
