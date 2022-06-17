package fr.ensimag.deca.tree;

/**
 * Visibility of a field.
 *
 * @author gl10
 * @date 25/04/2022
 */

public enum Visibility {
    PUBLIC (""),
    PROTECTED ("protected");

    private final String name;
    private Visibility(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
