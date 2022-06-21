package tools;

import org.apache.log4j.Logger;

public class DireBonjour extends SayHello {
    private static final Logger LOG = Logger.getLogger(DireBonjour.class);

    
    /** 
     * @return String
     */
    @Override
    public String getMessage() {
        LOG.trace("Appel de getMessage() dans DireBonjour");
        return "Bonjour";
    }

    
    /** 
     * @return String
     */
    @Override
    public String getOtherMessage() {
        LOG.trace("Appel de getOtherMessage() dans DireBonjour");
        return "Au revoir";
    }

}
