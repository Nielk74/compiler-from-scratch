package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class StackManager {
    private int gbOffsetCounter = 1;
    private int lbOffsetCounter = 1;
    private int savedRegisterCounter = 0;
    private int varCounter = 0;
    private int maxMethodCallParamNb = 0;

    // rename to resetStackCounters
    public void resetStackCounters() {
        lbOffsetCounter = 1;
        savedRegisterCounter = 0;
        varCounter = 0;
        maxMethodCallParamNb = 0;
    }

    public void incrementGbOffsetCounter() {
        this.gbOffsetCounter++;
    }

    public int getGbOffsetCounter() {
        return this.gbOffsetCounter;
    }

    public void incrementLbOffsetCounter() {
        this.lbOffsetCounter++;
    }

    public int getLbOffsetCounter() {
        return this.lbOffsetCounter;
    }

    public void setLbOffsetCounter(int lbOffsetCounter) {
        this.lbOffsetCounter = lbOffsetCounter;
    }

    public void incrementSavedRegisterCounter() {
        savedRegisterCounter++;
    }

    public int getSavedRegisterCounter() {
        return savedRegisterCounter;
    }

    public void incrementVarCounter() {
        varCounter++;
    }

    public int getVarCounter() {
        return this.varCounter;
    }

    public void setMaxMethodCallParamNb(int newMax) {
        if (newMax > this.maxMethodCallParamNb) {
            this.maxMethodCallParamNb = newMax;
        }
    }

    public int getStackSize() {
        return this.varCounter + this.maxMethodCallParamNb + this.savedRegisterCounter;
    }

    public void initializeStack(DecacCompiler compiler) {
        compiler.addFirst(new ADDSP(varCounter));
        if (!compiler.getCompilerOptions().getNocheck()) {
            compiler.addFirst(new BOV(compiler.labelManager.getLabel(ErrorCatcher.SO_ERROR)));
        }
        compiler.addFirst(new TSTO(getStackSize()));
        compiler.addFirst(new Line("stack initialization"));
    }
}
