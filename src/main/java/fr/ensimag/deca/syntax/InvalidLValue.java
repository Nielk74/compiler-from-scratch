package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Syntax error for an expression that should be an lvalue (ie that can be
 * assigned), but is not.
 *
 * @author gl10
 * 
 */
public class InvalidLValue extends DecaRecognitionException {

    private static final long serialVersionUID = 4670163376041273741L;
    private String type;

    /**
     * @param recognizer
     * @param ctx
     */
    public InvalidLValue(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
        this.type = "";
    }

    /**
     * @param recognizer
     * @param ctx
     * @param type
     */
    public InvalidLValue(DecaParser recognizer, ParserRuleContext ctx, String type) {
        super(recognizer, ctx);
        this.type = type;
    }


    
    /** 
     * @return String
     */
    @Override
    public String getMessage() {
        return "Wrong left value of assignment - expected: variable or field ≠ current: " + this.type;
    }
}
