package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the string type
 *
 * @author gl10
 * 
 */
public class BooleanType extends Type {

    public BooleanType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoolean() {
        return true;
    }

    
    /** 
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isBoolean();
    }


}
