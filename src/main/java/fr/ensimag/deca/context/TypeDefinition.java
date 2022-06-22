package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a Deca type (builtin or class).
 *
 * @author gl10
 * 
 */
public class TypeDefinition extends Definition {

    /**
     * @param type The type of the type that is being defined.
     * @param location The location of the definition of the variable in the Deca code.
     */
    public TypeDefinition(Type type, Location location) {
        super(type, location);
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public String getNature() {
        return "type";
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isExpression() {
        return false;
    }

}
