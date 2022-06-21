package fr.ensimag.deca.tree;

import java.io.Serializable;

/**
 * Location in a file (File, line, positionInLine).
 *
 * @author gl10
 * 
 */
public class Location implements Serializable {
    /*
     * Location implements Serializable because it appears as a field
     * of LocationException, which is serializable.
     */
    private static final long serialVersionUID = -2906437663480660298L;

    public static final String NO_SOURCE_NAME = "<no source file>";
    public static final Location BUILTIN = new Location(-1, -1, NO_SOURCE_NAME);

    /**
     * Display the (line, positionInLine) as a String. The file is not
     * displayed.
     * 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (this == BUILTIN) {
            return "[builtin]";
        } else {
            return "[" + line + ", " + positionInLine + "]";
        }
    }

    /**
     * Accesor: return the field line of the class.
     * 
     * @return int
     */
    public int getLine() {
        return line;
    }


    /**
     * Accesor: return the field positionInLine of the class.
     * 
     * @return int
     */
    public int getPositionInLine() {
        return positionInLine;
    }

    /**
     * Accesor: return the field filename (or NO_SOURCE_NAME if filename does not exist) of the class.
     * 
     * @return String
     */
    public String getFilename() {
        if (filename != null) {
            return filename;
        } else {
            // we're probably reading from stdin
            return NO_SOURCE_NAME;
        }
    }

    // line
    private final int line;
    // column 
    private final int positionInLine;
    // filename
    private final String filename;

    /**
    *@param line line in code
    *@param positionInLine column in code
    *@param filename
     */
    public Location(int line, int positionInLine, String filename) {
        super();
        this.line = line;
        this.positionInLine = positionInLine;
        this.filename = filename;
    }

}
