package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;

/**
 * Definition of a method parameter.
 *
 * @author gl10
 * 
 */
public class ParamDefinition extends ExpDefinition {

    /**
     * @param type Type of a methode parameter. 
     * @param location Its location in the Deca code.
     */
    public ParamDefinition(Type type, Location location) {
        super(type, location);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNature() {
        return "parameter";
    }

    
     /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExpression() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isParam() {
        return true;
    }

}
