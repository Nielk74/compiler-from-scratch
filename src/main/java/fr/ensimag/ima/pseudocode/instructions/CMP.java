package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

/**
 *
 * @author Ensimag
 * 
 */
public class CMP extends BinaryInstructionDValToReg {

    public CMP(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public CMP(int val, GPRegister op2) {
        this(new ImmediateInteger(val), op2);
    }

}