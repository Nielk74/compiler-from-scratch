package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RFLOAT;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorCatcher;
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
        if (this.getRightOperand() instanceof AbstractIdentifier) {
            // ABSTRACT IDENTIFIER
            AbstractIdentifier absIdent = (AbstractIdentifier) this.getRightOperand();
            compiler.addInstruction(new LOAD(absIdent.getExpDefinition().getOperand(), Register.getR(2)));
        } else if (this.getRightOperand() instanceof AbstractReadExpr) {
            // CAS DES READERS (READINT, READFLOAT)
            if (this.getRightOperand() instanceof ReadInt) {
                compiler.addInstruction(new RINT());
            } else if (this.getRightOperand() instanceof ReadFloat) {
                compiler.addInstruction(new RFLOAT());
            }
            compiler.addInstruction(new BOV(new Label(ErrorCatcher.IO_ERROR)));
            compiler.addInstruction(new LOAD(Register.R1, Register.getR(2)));
        } else if (getRightOperand().getType().isInt() || getRightOperand().getType().isFloat()) {
            getRightOperand().codeGenExp(compiler, 2);
        }
        // A check si on peut incrémenter le offset autrement
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
