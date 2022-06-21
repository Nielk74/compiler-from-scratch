package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the int type
 *
 * @author Ensimag
 * 
 */
public class IntType extends Type {

    /**
     * @param name The name of the type.
     */
    public IntType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInt() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isInt();
    }


}
