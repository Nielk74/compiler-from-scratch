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
    public static final String OV_ERROR = "ov_error";
    public static final String UD_ERROR = "ud_error";
    public static final String HO_ERROR = "ho_error";

    public static void createErrorLabel(DecacCompiler compiler) {
        compiler.labelManager.createLabel(IO_ERROR);
        compiler.labelManager.createLabel(SO_ERROR);
        compiler.labelManager.createLabel(OV_ERROR);
        compiler.labelManager.createLabel(UD_ERROR);

        compiler.labelManager.createLabel(HO_ERROR);
    }

    public static void handleErrors(DecacCompiler compiler) {
        // IO_ERROR
        compiler.addLabel(compiler.labelManager.getLabel(IO_ERROR));
        addErrorHandler(compiler, "Error: Input/Output error");

        // SO_ERROR
        compiler.addLabel(compiler.labelManager.getLabel(SO_ERROR));
        addErrorHandler(compiler, "Error: Stack Overflow");

        // OV_ERROR
        compiler.addLabel(compiler.labelManager.getLabel(OV_ERROR));
        addErrorHandler(compiler, "Error: Overflow");

        // UD_ERROR
        compiler.addLabel(compiler.labelManager.getLabel(UD_ERROR));
        addErrorHandler(compiler, "Error: Underflow");
        // HO_ERROR
        compiler.addLabel(compiler.labelManager.getLabel(HO_ERROR));
        addErrorHandler(compiler, "Error: Heap Overflow");
    }

    private static void addErrorHandler(DecacCompiler compiler, String message) {
        compiler.addInstruction(new WSTR(message));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
    }

    private ErrorCatcher() {
    }
}