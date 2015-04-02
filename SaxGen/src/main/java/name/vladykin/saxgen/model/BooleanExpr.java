package name.vladykin.saxgen.model;

import java.util.List;

/**
 * @author Alexey Vladykin
 */
public class BooleanExpr implements Expr {

    private final List<Expr> operands;
    private final Operator operator;

    public BooleanExpr(List<Expr> operands, Operator operator) {
        this.operands = operands;
        this.operator = operator;
    }

    public int getOperandCount() {
        return operands.size();
    }

    public Expr getOperand(int i) {
        return operands.get(i);
    }

    public Operator getOperator() {
        return operator;
    }
    
    public static enum Operator {
        AND,
        OR
    }

}
