package name.vladykin.saxgen.model;

import java.util.List;

/**
 * @author Alexey Vladykin
 */
public class Group implements InputElement {

    private final List<List<InputElement>> alternatives;
    private final Quantifier quantifier;

    public Group(List<List<InputElement>> a, Quantifier q) {
        this.alternatives = a;
        this.quantifier = q;
    }

    public Type getType() {
        return Type.GROUP;
    }

    public int getAlternativeCount() {
        return alternatives.size();
    }

    public List<InputElement> getAlternative(int i) {
        return alternatives.get(i);
    }

    public Quantifier getQuantifier() {
        return quantifier;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('(');
        for (int i = 0; i < alternatives.size(); ++i) {
            if (0 < i) {
                buf.append(" | ");
            }
            buf.append(alternatives.get(i));
        }
        buf.append(')');
        switch (quantifier) {
            case QUESTION:
                buf.append('?');
                break;
            case ASTERISK:
                buf.append('*');
                break;
        }
        return buf.toString();
    }

    public static enum Quantifier {
        NONE,
        QUESTION,
        ASTERISK
    }

    public static Quantifier quantifier(String q) {
        if (q == null) {
            return Quantifier.NONE;
        } else if (q.equals("?")) {
            return Quantifier.QUESTION;
        } else if (q.equals("*")) {
            return Quantifier.ASTERISK;
        } else {
            return null;
        }
    }
}
