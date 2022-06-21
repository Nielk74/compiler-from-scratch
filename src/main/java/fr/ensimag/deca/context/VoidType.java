package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable;

/**
 * Definition of the void type
 * 
 * @author gl10
 * 
 */
public class VoidType extends Type {

    /**
     * @param name The name of the type.
     */
    public VoidType(SymbolTable.Symbol name) {
        super(name);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVoid() {
        return true;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sameType(Type otherType) {
        return otherType.isVoid();
    }


}
