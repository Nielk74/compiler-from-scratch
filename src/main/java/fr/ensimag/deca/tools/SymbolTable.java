package fr.ensimag.deca.tools;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage unique symbols.
 * 
 * A Symbol contains the same information as a String, but the SymbolTable
 * ensures the uniqueness of a Symbol for a given String value. Therefore,
 * Symbol comparison can be done by comparing references, and the hashCode()
 * method of Symbols can be used to define efficient HashMap (no string
 * comparison or hashing required).
 * 
 * @author gl10
 * 
 */
public class SymbolTable {
    private Map<String, Symbol> map = new HashMap<String, Symbol>();

    /**
     * Precondition: name != null
     * Create or reuse a symbol.
     * 
     * If a symbol already exists with the same name in this table, then return
     * this Symbol. Otherwise, create a new Symbol and add it to the table.
     * 
     * @param name Name of the symbol in the Deca code.
     * @return Symbol
     */
    public Symbol create(String name) {
        if (name == null)
            throw new IllegalArgumentException("Wrong symbol type - expected: string ≠ current: null");
        Symbol s = map.get(name);
        if (s == null) {
            s = new Symbol(name);
            map.put(name, s);
        }
        return s;
    }

    public static class Symbol {
        /**
         * Constructor is private, so that Symbol instances can only be created
         * through SymbolTable.create factory (which thus ensures uniqueness
         * of symbols).
         * 
         * @param name Name of the symbol in the Deca code.
         */
        private Symbol(String name) {
            super();
            this.name = name;
        }

        /**
         * Returne the name in the Deca code, of the symbol.
         * @return String
         */
        public String getName() {
            return name;
        }
        
        /**
         * {@inheritDoc}
         * Return the name in String type
         */
        @Override
        public String toString() {
            return name;
        }

        private String name;
    }
}
