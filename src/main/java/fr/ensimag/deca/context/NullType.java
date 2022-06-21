package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the string type
 * 
 * @author Ensimag
 * 
 */
public class NullType extends Type {

    /**
     * @param name The name of the type.
     */
    public NullType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isNull();
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClassOrNull() {
        return true;
    }


}
