package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a variable.
 *
 * @author gl10
 * 
 */
public class VariableDefinition extends ExpDefinition {
    /**
     * @param type The type of the variable.
     * @param location The location of the definition of the variable in the Deca code.
     */
    public VariableDefinition(Type type, Location location) {
        super(type, location);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNature() {
        return "variable";
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean isExpression() {
        return true;
    }
}
