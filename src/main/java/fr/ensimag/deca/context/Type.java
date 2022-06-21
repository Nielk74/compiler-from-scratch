package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Deca Type (internal representation of the compiler)
 *
 * @author gl10
 * 
 */

public abstract class Type {

    /**
     * The symbol of the type, its name in the Deca language.
     */
    private final Symbol name;
    
    /**
     * @param name The name of the type in the Deca code.
     */
    public Type(Symbol name) {
        this.name = name;
    }

     /**
     * True if this and otherType represent the same type (in the case of
     * classes, this means they represent the same class).
     * 
     * @param otherType The other type that we compare with this type.
     * @return boolean
     */
    public abstract boolean sameType(Type otherType);

    /** 
     * Return the symbole of the type, its name in the Deca language.
     * @return Symbol
     */
    public Symbol getName() {
        return name;
    }

    
    /** 
     * {@inheritDoc}
     * Return the symbol in String type.
     */
    @Override
    public String toString() {
        return getName().toString();
    }

    
    /** 
     * Return true if the type is a class, otherwise false.
     * @return boolean
     */
    public boolean isClass() {
        return false;
    }

    
    /** 
     * Return true if the type is int, otherwise false.
     * @return boolean
     */
    public boolean isInt() {
        return false;
    }

    
    /** 
     * Return true if the type is float, otherwise false.
     * @return boolean
     */
    public boolean isFloat() {
        return false;
    }

    
    /** 
     * Return true if the type is boolean, otherwise false.
     * @return boolean
     */
    public boolean isBoolean() {
        return false;
    }

    
    /** 
     * Return true if the type is void, otherwise false.
     * @return boolean
     */
    public boolean isVoid() {
        return false;
    }

    
    /** 
     * Return true if the type is string, otherwise false.
     * @return boolean
     */
    public boolean isString() {
        return false;
    }

    
    /** 
     * Return true if the type null, otherwise false.
     * @return boolean
     */
    public boolean isNull() {
        return false;
    }

    
    /** Return true if the type is a class or null, otherwise false.
     * @return boolean
     */
    public boolean isClassOrNull() {
        return false;
    }

    /**
     * Returns the same object, as type ClassType, if possible. Throws
     * ContextualError(errorMessage, l) otherwise.
     *
     * Can be seen as a cast, but throws an explicit contextual error when the
     * cast fails.
     * @param errorMessage The message error to throw.
     * @param l The location where the error is thrown in the Deca code.
     * @return ClassType
     */
    public ClassType asClassType(String errorMessage, Location l)
            throws ContextualError {
        throw new ContextualError(errorMessage, l);
    }

}
