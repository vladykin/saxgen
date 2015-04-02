package name.vladykin.saxgen.model;

/**
 * Base class for StartTag and EndTag.
 *
 * @author Alexey Vladykin
 */
public abstract class Tag implements InputElement {

    private final String name;
    private final String code;

    public Tag(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
