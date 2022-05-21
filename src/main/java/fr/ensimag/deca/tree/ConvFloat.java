package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl10
 * @date 25/04/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        this.setType(compiler.environmentType.FLOAT);
        return this.getType();
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

    // evalue l'expression et stocke son résultat dans le registre
    // Register.getR(register_name)
    @Override
    public void codeGenExp(DecacCompiler compiler, int register_name) {
        this.getOperand().codeGenExp(compiler, register_name);
        compiler.addInstruction(new FLOAT(Register.getR(register_name), Register.getR(register_name)));
    }
}
