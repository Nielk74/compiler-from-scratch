package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the float type
 *
 * @author Ensimag
 * 
 */
public class FloatType extends Type {

    /**
     * @param name The name of the type.
     */
    public FloatType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloat() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isFloat();
    }


}
