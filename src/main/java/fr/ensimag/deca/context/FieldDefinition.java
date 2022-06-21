package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.Visibility;

/**
 * Definition of a field (data member of a class).
 *
 * @author gl10
 * 
 */
public class FieldDefinition extends ExpDefinition {

    /**
     * @param visibility Visibility of the field.
     */
    private final Visibility visibility;
    /**
     * @param containingClass The class containing the field.
     */
    private final ClassDefinition containingClass;

    /**
     * @param type Type of the field.
     * @param location Location of the field definition in the Deca code.
     * @param visibility Visibility of the field definition.
     * @param memberOf The class containing the field.
     * @param index The nth field of the class.
     */
    public FieldDefinition(Type type, Location location, Visibility visibility,
            ClassDefinition memberOf, int index) {
        super(type, location);
        this.visibility = visibility;
        this.containingClass = memberOf;
        this.index = index;
    }

    /** 
     * Return the index of the field definition.
     * 
     * @return int
     */
    public int getIndex() {
        return index;
    }

    private int index;
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isField() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public FieldDefinition asFieldDefinition(String errorMessage, Location l)
            throws ContextualError {
        return this;
    }

    
    /** 
     * Return the Visibility of the field.
     * 
     * @return Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    
    /** 
     * Return the ClassDefinition of the field.
     * 
     * @return ClassDefinition
     */
    public ClassDefinition getContainingClass() {
        return containingClass;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNature() {
        return "field";
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpression() {
        return true;
    }

}
