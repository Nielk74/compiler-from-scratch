package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * 
 */
public abstract class Instruction {
    String getName() {
        return this.getClass().getSimpleName();
    }
    abstract void displayOperands(PrintStream s);
    void display(PrintStream s) {
        s.print(getName());
        displayOperands(s);
    }
}
