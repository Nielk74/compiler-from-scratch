package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of an identifier.
 * 
 * @author gl10
 * 
 */
public abstract class Definition {
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String res;
        res = getNature();
        if (location == Location.BUILTIN) {
            res += " (builtin)";
        } else {
            res += " defined at " + location;
        }
        res += ", type=" + type;
        return res;
    }

    /** 
     * Return the nature of the definition.-
     * @return String
     */
    public abstract String getNature();

    /** 
     * @param type Type of the definition. 
     * @param location of the definition in the code
     */
    public Definition(Type type, Location location) {
        super();
        this.location = location;
        this.type = type;
    }

    
    /** 
     * Return the type of the definition. 
     * @return Type
     */
    public Type getType() {
        return type;
    }

    
    /** 
     * Return the location of the definition in the code. 
     * @return Location
     */
    public Location getLocation() {
        return location;
    }

    
    /** 
     * Set the location of the definition in the code
     * @param location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /** 
     * @param location Location of the definition in the code. 
     */
    private Location location;
    /** 
     * @param type Type of the definition.
     */
    private Type type;
    
    /** 
     * Return true if the Definition is a Field Definition, false otherwise. 
     * @return boolean
     */
    public boolean isField() {
        return false;
    }
    
    
    /** 
     * Return true if the Definition is a Method Definition, false otherwise. 
     * @return boolean
     */
    public boolean isMethod() {
        return false;
    }

    
    /** 
     * Return true if the Definition is a Class Definition, false otherwise. 
     * @return boolean
     */
    public boolean isClass() {
        return false;
    }

    
    /** 
     * Return true if the Definition is a Param Definition, false otherwise. 
     * @return boolean
     */
    public boolean isParam() {
        return false;
    }

    /**
     * Return the same object, as type MethodDefinition, if possible. Throws
     * ContextualError(errorMessage, l) otherwise.
     * @param errorMessage Message of the error. 
     * @param l Location of the definition in the code. 
     */
    public MethodDefinition asMethodDefinition(String errorMessage, Location l)
            throws ContextualError {
        throw new ContextualError(errorMessage, l);
    }
    
    /**
     * Return the same object, as type FieldDefinition, if possible. Throws
     * ContextualError(errorMessage, l) otherwise.
     * @param errorMessage Message of the error. 
     * @param l Location of the definition in the code. 
     */
    public FieldDefinition asFieldDefinition(String errorMessage, Location l)
            throws ContextualError {
        throw new ContextualError(errorMessage, l);
    }

    /** 
     * Return true if the Definition is an expression, false otherwise. 
     * @return boolean
     */
    public abstract boolean isExpression();

}
