package ru.vladykin.saxgen.model;

import ru.vladykin.saxgen.model.InputElement.Type;

/**
 * @author Alexey Vladykin
 */
public class EndTag extends Tag {

    public EndTag(String name, String code) {
        super(name, code);
    }

    public Type getType() {
        return Type.END_TAG;
    }

    @Override
    public String toString() {
        return "</" + getName() + ">";
    }
}
