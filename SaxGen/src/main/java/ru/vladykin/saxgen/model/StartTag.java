package ru.vladykin.saxgen.model;

/**
 * @author Alexey Vladykin
 */
public class StartTag extends Tag {

    private final Expr condition;

    public StartTag(String name, Expr condition, String code) {
        super(name, code);
        this.condition = condition;
    }

    public Type getType() {
        return Type.START_TAG;
    }

    public Expr getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('<').append(getName()).append('>');
        return buf.toString();
    }
}
