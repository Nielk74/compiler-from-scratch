package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Main block of a Deca program (code whiwh is not in any class).
 * @author gl10
 * 
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    // list of variables declarations
    private ListDeclVar declVariables;
    // list of instructions
    private ListInst insts;
    /**
    * @param declVariables
    * @param insts
     */
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify Main: start");
        EnvironmentExp localEnv = new EnvironmentExp(null);
        this.declVariables.verifyListDeclVariable(compiler, localEnv, null);
        this.insts.verifyListInst(compiler, localEnv, null, compiler.environmentType.VOID);
        LOG.debug("verify Main: end");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void codeGenMain(DecacCompiler compiler) {
        // update LB offset with the GB offset
        compiler.stackManager.setLbOffsetCounter(compiler.stackManager.getGbOffsetCounter());
        compiler.addComment("Beginning of main instructions:");
        declVariables.codeGenListDeclVariable(compiler);
        insts.codeGenListInst(compiler);
        compiler.addComment("End of main instructions:");
    }

     /**
     * {@inheritDoc}
     */   
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    /**
     * {@inheritDoc}
     */ 
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
