package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the string type
 * 
 * @author gl10
 * 
 */
public class StringType extends Type {

    /**
     * @param name The name of the type.
     */
    public StringType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isString() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isString();
    }
}
