package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author Ensimag
 * 
 */
public class DIV extends BinaryInstructionDValToReg {

    public DIV(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

}
