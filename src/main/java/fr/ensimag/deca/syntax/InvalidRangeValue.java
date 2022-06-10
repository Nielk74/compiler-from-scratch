package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidRangeValue extends DecaRecognitionException {
    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidRangeValue(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Value out of range";
    }
}
