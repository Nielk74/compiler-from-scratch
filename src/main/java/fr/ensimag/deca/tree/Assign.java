package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
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
            AbstractIdentifier absIdent = (AbstractIdentifier) this.getRightOperand();
            compiler.addInstruction(new LOAD(absIdent.getExpDefinition().getOperand(), Register.getR(2)));
        }
        else if (getRightOperand().getType() != null) {
            if (getRightOperand().getType().isInt()) {
                IntLiteral intLiteral = (IntLiteral) getRightOperand();
                compiler.addInstruction(new LOAD(new ImmediateInteger(intLiteral.getValue()), Register.getR(2)));
            } else if (getRightOperand().getType().isFloat()) {
                if (this.getRightOperand() instanceof FloatLiteral) {
                    FloatLiteral floatLiteral = (FloatLiteral) getRightOperand();
                    compiler.addInstruction(new LOAD(new ImmediateFloat(floatLiteral.getValue()), Register.getR(2)));
                } else if (this.getRightOperand() instanceof ConvFloat) {
                    IntLiteral intLiteral = (IntLiteral) ((ConvFloat) this.getRightOperand()).getOperand();
                    compiler.addInstruction(
                            new LOAD(new ImmediateFloat((float) intLiteral.getValue()), Register.getR(2)));
                }
            }
        }
        // A check si on peut incrémenter le offset autrement
        RegisterOffset offset = new RegisterOffset(Register.getLbOffsetCounter(), Register.LB);
        Register.incrementLbOffsetCounter();
        compiler.addInstruction(new STORE(Register.getR(2), offset));
        ((AbstractIdentifier) this.getLeftOperand()).getExpDefinition().setOperand(offset);
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
