package fr.ensimag.deca.tree;

import java.io.PrintStream;

/**
 * Exception corresponding to an error at a particular location in a file.
 *
 * @author gl10
 * 
 */
public class LocationException extends Exception {

    /**
     * Accesor: return the field location of the class.
     * 
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Display error with its location: filename, line and colum
     * 
     * @param s standard output(stdout)
     */
    public void display(PrintStream s) {
        Location loc = getLocation();
        String line;
        String column;
        if (loc == null) {
            line = "<unknown>";
            column = "";
        } else {
            line = Integer.toString(loc.getLine());
            column = ":" + loc.getPositionInLine();
        }
        s.println(location.getFilename() + ":" + line + column + ": " + getMessage());
    }

    private static final long serialVersionUID = 7628400022855935597L;
    // error's location
    protected Location location;

    /**
     * @param message
     * @param location
     */
    public LocationException(String message, Location location) {
        super(message);
        assert(location == null || location.getFilename() != null);
        this.location = location;
    }

}
