package fr.ensimag.deca.context;

import java.util.HashMap;
import java.util.Map;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * 
 * Insertion (through method declare) is always done in the "current"
 * dictionary.
 * 
 * @author gl10
 * 
 */
public class EnvironmentExp {
    // DONE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    /** 
     * @param dictionary Data structure representing an environment (association name -> definition, with possibility of stacking).
     */
    private Map<Symbol, ExpDefinition> dictionary = new HashMap<>();
    /** 
     * @param dictionary Environment parent. 
     */
    EnvironmentExp parentEnvironment;

    /** 
     * @param parentEnvironment Environment parent. 
     */
    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }

    /**
    * Exception raised when a definition is declared as a duplicate.
    * 
    * @author gl10
    * 
    */
    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;

        /** 
        * @param message Message of the error.
        */
        public DoubleDefException(String message) {
            super(message);
        }
    }

    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     * @param key Potential key of the hashmap to find a definition. 
     */
    public ExpDefinition get(Symbol key) {
        ExpDefinition expDef = this.dictionary.get(key);
        if (expDef != null)
            return expDef;
        else {
            if (this.parentEnvironment != null) {
                return this.parentEnvironment.get(key);
            }
        }
        return null;
    }

    /**
     * Return the definition of the method at the given index in the class or the super class, or null if the
     * index doesn't exist in the class and the super class.
     * @param index Potential index of a method definition. 
     */
    public MethodDefinition getMethod(int index) {
        for (ExpDefinition expDef : this.dictionary.values()) {
            if (expDef.isMethod()) {
                if (((MethodDefinition) expDef).getIndex() == index) {
                    return (MethodDefinition) expDef;
                }
            }
        }
        if (this.parentEnvironment != null) {
            return this.parentEnvironment.getMethod(index);
        }
        return null;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *             Name of the symbol to define
     * @param def
     *             Definition of the symbol
     * @throws DoubleDefException
     *                            if the symbol is already defined at the "current"
     *                            dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (this.dictionary.get(name) != null)
            throw new DoubleDefException("Wrong variable name: "+ name.getName() + " is already defined");
        this.dictionary.put(name, def);
    }
}
