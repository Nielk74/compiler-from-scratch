package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
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
    public static final String SO_ERROR = "so_error";

    // overflow_error
    // stack_overflow_error

    public static void handleErrors(DecacCompiler compiler) {
        Label io_error_label = compiler.labelManager.createLabel(IO_ERROR);
        Label so_error_label = compiler.labelManager.createLabel(SO_ERROR);

        // IO_ERROR
        compiler.addLabel(io_error_label);
        addErrorHandler(compiler, "Error: Input/Output error");

        // SO_ERROR
        compiler.addLabel(so_error_label);
        addErrorHandler(compiler, "Error: Stack Overflow");
    }

    private static void addErrorHandler(DecacCompiler compiler, String message) {
        compiler.addInstruction(new WSTR(message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    private ErrorCatcher() {
    }
}