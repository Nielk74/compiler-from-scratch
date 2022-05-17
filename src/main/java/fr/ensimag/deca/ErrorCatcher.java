package fr.ensimag.deca;

import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.ERROR;

/**
 * @author gl10
 * @date 25/04/2022
 */
public class ErrorCatcher {
    public static final String IO_ERROR = "io_error";

    // overflow_error
    // stack_overflow_error

    public static void handleErrors(DecacCompiler compiler) {
        Label io_error_label = new Label(IO_ERROR);

        // IO_ERROR
        compiler.addLabel(io_error_label);
        compiler.addInstruction(new WSTR("Error: Input/Output error"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    private ErrorCatcher() {
    }
}