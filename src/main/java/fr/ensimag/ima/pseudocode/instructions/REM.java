package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author Ensimag
 * 
 */
public class REM extends BinaryInstructionDValToReg {

    public REM(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

}
