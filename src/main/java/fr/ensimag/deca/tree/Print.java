package fr.ensimag.deca.tree;

/**
 * Print.
 * 
 * @author gl10
 * 
 */
public class Print extends AbstractPrint {
    /**
     * @param arguments arguments passed to the print(...) statement.
     * @param printHex if true, then float should be displayed as hexadecimal (printx)
     */
    public Print(boolean printHex, ListExpr arguments) {
        super(printHex, arguments);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getSuffix() {
        return "";
    }
}
