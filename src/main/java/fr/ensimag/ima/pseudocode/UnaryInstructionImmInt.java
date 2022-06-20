package fr.ensimag.ima.pseudocode;

/**
 *
 * @author Ensimag
 * @date 25/04/2022
 */
public abstract class UnaryInstructionImmInt extends UnaryInstruction {

    protected UnaryInstructionImmInt(ImmediateInteger operand) {
        super(operand);
    }

    protected UnaryInstructionImmInt(int i) {
        super(new ImmediateInteger(i));
    }

    public void setValue(int i) {
        super.setOperand(new ImmediateInteger(i));
    }
}
