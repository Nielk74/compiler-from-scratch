package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl10
 * 
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    
    /** 
     * Add a new input to the signature of the methode.
     * @param t Type of the input parameter to add.
     */
    public void add(Type t) {
        args.add(t);
    }
    
    
    /** 
     * Return the type of the nth parameter of the methode.
     * @param n The number of the desired parameter. 
     * @return Type
     */
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    
    /** 
     * Return the number of parameter of the signature of the methode.
     * @return int
     */
    public int size() {
        return args.size();
    }

    /**
     * Verify if the signature of the 2 methodes are the same.
     * @param otherSig The signature of the other methode to compare with this signature.
     */
    public boolean sameSignature(Signature otherSig) {
        if (args.size() != otherSig.size()){
            return false;
        }
        else {
            for (int index = 0; index < size(); index++) {
                if (!paramNumber(index).sameType(otherSig.paramNumber(index))){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * Return the different types of the signature in String type.
     */
    @Override
    public String toString() {
        return args.toString();
    }
}
