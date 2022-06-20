package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }

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

    @Override
    public String toString() {
        return args.toString();
    }
}
