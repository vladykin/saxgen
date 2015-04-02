package ru.vladykin.saxgen.model;

/**
 * @author Alexey Vladykin
 */
public interface InputElement {

    static enum Type {
        START_TAG,
        END_TAG,
        GROUP
    }

    Type getType();

}
