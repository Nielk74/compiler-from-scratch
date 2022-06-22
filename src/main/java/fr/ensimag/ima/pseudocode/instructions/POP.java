package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstructionToReg;

/**
 *
 * @author Ensimag
 * 
 */
public class POP extends UnaryInstructionToReg {

    public POP(GPRegister op) {
        super(op);
    }

}
