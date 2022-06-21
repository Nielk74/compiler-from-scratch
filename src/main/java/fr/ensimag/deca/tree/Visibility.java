package fr.ensimag.deca.tree;

/**
 * Visibility of a field.
 *
 * @author gl10
 * 
 */

public enum Visibility {
    /**
     * How is represented the public and protected visibility in the Deca code.
     */
    PUBLIC (""),
    PROTECTED ("protected");

    /**
     * Name of the visibility.
     */
    private final String name;

    /**
     * @param name Name of the visibility.
     */
    private Visibility(String name) {
        this.name = name;
    }

    /**
     * return the name of the visibility in string type.
     * 
     * @return String
     */
    public String toString() {
        return name;
    }
}
