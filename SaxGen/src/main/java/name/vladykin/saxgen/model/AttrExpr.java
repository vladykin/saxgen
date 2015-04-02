package name.vladykin.saxgen.model;

/**
 * @author Alexey Vladykin
 */
public class AttrExpr implements Expr {

    private final String attribute;
    private final String value;
    private final Operator operator;

    public AttrExpr(String a, String v, Operator o) {
        this.attribute = a;
        this.value = v;
        this.operator = o;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

    public Operator getOperator() {
        return operator;
    }

    public static enum Operator {
        EQUALS,
        NOT_EQUALS,
        SUBSTR_EQUALS,
        NOT_SUBSTR_EQUALS
    }

    public static String value(String s) {
        if (s == null || s.equals("null")) {
            return null;
        } else if (0 < s.length() && s.charAt(0) == '"') {
            return s.substring(1, s.length() - 1);
        } else {
            throw new IllegalArgumentException();
        }
    }

}
