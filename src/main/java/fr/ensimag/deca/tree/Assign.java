package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl10
 * @date 25/04/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        ExpDefinition leftDef = localEnv.get(((AbstractIdentifier) this.getLeftOperand()).getName());
        if (leftDef == null)
            throw new ContextualError(
                    "Lvalue " + ((AbstractIdentifier) this.getLeftOperand()).getName().getName() + " does not exist",
                    this.getLeftOperand().getLocation());

        ((AbstractIdentifier) this.getLeftOperand()).setDefinition(leftDef);
        Type leftType = leftDef.getType();
        AbstractExpr expr = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        this.setRightOperand(expr);
        return leftType;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        this.getRightOperand().codeGenExp(compiler, 2);
        DAddr offset = ((AbstractIdentifier) this.getLeftOperand()).getExpDefinition().getOperand();
        if (offset == null) {
            offset = new RegisterOffset(Register.getLbOffsetCounter(), Register.LB);
            Register.incrementLbOffsetCounter();
        }
        compiler.addInstruction(new STORE(Register.getR(2), offset));
        ((AbstractIdentifier) this.getLeftOperand()).getExpDefinition().setOperand(offset);
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
