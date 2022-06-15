package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class StackManager {
    private int varCounter = 0;
    private int lbOffsetCounter = 1;
    private int gbOffsetCounter = 1;

    public void incrementVarCounter() {
        varCounter++;
    }

    public void initializeStack(DecacCompiler compiler) {
        compiler.addFirst(new ADDSP(varCounter));
        compiler.addFirst(new BOV(compiler.labelManager.getLabel(ErrorCatcher.SO_ERROR)));
        compiler.addFirst(new TSTO(varCounter));
        compiler.addFirst(new Line("stack initialization"));
    }

    public void incrementLbOffsetCounter(){
        this.lbOffsetCounter++;
    }

    public void incrementGbOffsetCounter(){
        this.gbOffsetCounter++;
    }

    public int getLbOffsetCounter(){
        return this.lbOffsetCounter;
    }

    public int getGbOffsetCounter(){
        return this.gbOffsetCounter;
    }

    public void setLbOffsetCounter(int lbOffsetCounter){
        this.lbOffsetCounter = lbOffsetCounter;
    }
}
