package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidRangeValue extends DecaRecognitionException {
    private static final long serialVersionUID = 4670163376041273741L;

    /** 
     * @param recognizer 
     * @param ctx 
     */
    public InvalidRangeValue(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    
    /** 
     * Return the message of the error. 
     * @return String
     */
    @Override
    public String getMessage() {
        return "Value out of range";
    }
}
