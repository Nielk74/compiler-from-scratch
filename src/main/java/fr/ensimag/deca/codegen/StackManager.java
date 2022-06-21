package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

/**
 * The stack manager manages the initialization, and the use of the stack in the instruction blocks.
 * 
 * @author gl10
 */
public class StackManager {
    /**
     * Counter of the number of variables.
     */
    private int varCounter = 0;
    /**
     * Counter of the lbOffset.
     */
    private int lbOffsetCounter = 1;
    /**
     * Counter of the gbOffset. 
     */
    private int gbOffsetCounter = 1;
    /**
     * Max number of parameters for the called method.  
     */
    private int maxMethodCallParamNb = 0;
    /**
     * Counter of saved registers.
     */
    private int savedRegisterCounter = 0;

    /**
     * Increment the varCounter variable.
     */
    public void incrementVarCounter() {
        varCounter++;
    }

    /**
     * Reset the varCounter variable.
     */
    public void resetStackCounters() {
        lbOffsetCounter = 1;
        savedRegisterCounter = 0;
        varCounter = 0;
        maxMethodCallParamNb = 0;
    }

    /**
     * Increment the gbOffsetCounter variable.
     */
    public void incrementGbOffsetCounter() {
        this.gbOffsetCounter++;
    }

    /**
     * Return the gbOffsetCounter variable.
     * 
     * @return int
     */
    public int getGbOffsetCounter() {
        return this.gbOffsetCounter;
    }
    /**
     * Increment the lbOffsetCounter variable.
     */
    public void incrementLbOffsetCounter() {
        this.lbOffsetCounter++;
    }

    /**
     * Return the lbOffsetCounter variable.
     * 
     * @return int
     */
    public int getLbOffsetCounter() {
        return this.lbOffsetCounter;
    }

    /**
     * set the lbOffsetCounter variable, with the value lbOffsetCounter given.
     * 
     * @param lbOffsetCounter
     */
    public void setLbOffsetCounter(int lbOffsetCounter) {
        this.lbOffsetCounter = lbOffsetCounter;
    }

    /**
     * Increment the savedRegisterCounter variable.
     */
    public void incrementSavedRegisterCounter() {
        savedRegisterCounter++;
    }

    /**
     * Return the savedRegisterCounter variable.
     * @return int
     */
    public int getSavedRegisterCounter() {
        return savedRegisterCounter;
    }

    /**
     * Return the varCounter variable.
     * 
     * @return int
     */
    public int getVarCounter() {
        return this.varCounter;
    }
    /**
     * set the maxMethodCallParamNb variable, with the value newMax given.
     * 
     * @param newMax
     */
    public void setMaxMethodCallParamNb(int newMax) {
        if (newMax > this.maxMethodCallParamNb) {
            this.maxMethodCallParamNb = newMax;
        }
    }

    /**
     * Return the stack size.
     * 
     * @return int
     */
    public int getStackSize() {
        return this.varCounter + this.maxMethodCallParamNb + this.savedRegisterCounter;
    }

    /**
     * Initialize the Stack.
     * 
     * @param compiler
     */
    public void initializeStack(DecacCompiler compiler) {
        compiler.addFirst(new ADDSP(varCounter));
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addFirst(new BOV(compiler.labelManager.getLabel(ErrorCatcher.SO_ERROR)));
        }
        compiler.addFirst(new TSTO(getStackSize()));
        compiler.addFirst(new Line("stack initialization"));
    }
}
