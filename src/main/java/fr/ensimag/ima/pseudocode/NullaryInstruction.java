package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * Instruction without operand.
 *
 * @author Ensimag
 * 
 */
public abstract class NullaryInstruction extends Instruction {
    @Override
    void displayOperands(PrintStream s) {
        // no operand
    }
}
