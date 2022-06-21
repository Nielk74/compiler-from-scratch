package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl10
 * 
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    /**
     * @param classes The classes of the program.
     * @param main The main of the program.
     */
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    /**
     * Return the classes of the program.
     */
    public ListDeclClass getClasses() {
        return classes;
    }

    /*
     * Return the main of the program.
     * 
     * @return AbstractMain
     */
    public AbstractMain getMain() {
        return main;
    }

    /**
     * The different classes of the program.
     */
    private ListDeclClass classes;
    /**
     * The main of the program.
     */
    private AbstractMain main;

    /**
     * {@inheritDoc}
     */
    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify class: start");
        this.classes.verifyListClass(compiler);
        LOG.debug("verify class: end");
      
        LOG.debug("verify program: start");
        this.main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        classes.codeGenListDeclClass(compiler);
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.stackManager.initializeStack(compiler);
        compiler.addComment("End main program");
        classes.codeGenMethodImplementation(compiler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
