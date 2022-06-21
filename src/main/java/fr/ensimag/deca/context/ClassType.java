package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

/**
 * Type defined by a class.
 *
 * @author gl10
 * 
 */
public class ClassType extends Type {
    
    /** 
     * The definition of the class. 
     */
    protected ClassDefinition definition;
    
    
    /** 
     * Return the definition of the class.
     * @return ClassDefinition.
     */
    public ClassDefinition getDefinition() {
        return this.definition;
    }
            
    
    /** 
     * {@inheritDoc}
     */
    @Override
    public ClassType asClassType(String errorMessage, Location l) {
        return this;
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isClass() {
        return true;
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isClassOrNull() {
        return true;
    }

    /**
     * Standard creation of a type class.
     */
    public ClassType(Symbol className, Location location, ClassDefinition superClass) {
        super(className);
        this.definition = new ClassDefinition(this, location, superClass);
    }

    /**
     * Creates a type representing a class className.
     * (To be used by subclasses only)
     * @param className Name of the class.
     */
    protected ClassType(Symbol className) {
        super(className);
    }
    
    /** 
     * Return true if the type in parameter is the same as the type of the class. Return false otherwise.
     * @param otherType A type to compare with the type of the class.
     * @return boolean
     */
    @Override
    public boolean sameType(Type otherType) {
        if (otherType.isClassOrNull()) {
            return this.getName().equals(otherType.getName());
        } else {
            return false;
        }
    }

    /**
     * Return true if potentialSuperClass is a superclass of this class.
     * @param potentialSuperClass A potential super class of the class.
     */
    public boolean isSubClassOf(ClassType potentialSuperClass) {
        if (this.sameType(potentialSuperClass)) {
            return true;
        }
        ClassDefinition tempClass = getDefinition().getSuperClass();

        while (tempClass != null && !tempClass.equals(potentialSuperClass.getDefinition()) ){
            tempClass = tempClass.getSuperClass();
        }
        if (tempClass == null){
            return false;
        } else {
            return true;
        }
    }
}
