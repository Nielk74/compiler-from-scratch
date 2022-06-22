package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl10
 */
public class CompilerOptions {

    /**
     * Definition of some static int, for the options.
     */
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    
    /**
     *  Return the debug variable.
     * 
     * @return int
     */
    public int getDebug() {
        return debug;
    }

    
    /**
     * Return the parse variable. 
     * 
     * @return boolean
     */
    public boolean getParse() {
        return parse;
    }

    
    /** 
     * Return the verification variable.
     * 
     * @return boolean
     */
    public boolean getVerification() {
        return verification;
    }

    
    /** 
     * Return the parallel variable.
     * 
     * @return boolean
     */
    public boolean getParallel() {
        return parallel;
    }

    
    /**
     * Return the printBanner variable. 
     * 
     * @return boolean
     */
    public boolean getPrintBanner() {
        return printBanner;
    }

    
    /** 
     * Return the noCheck variable.
     * 
     * @return boolean
     */
    public boolean getNocheck() {
        return noCheck;
    }

    
    /** 
     * Return the registerMax variable.
     * 
     * @return int
     */
    public int getRegisterMax() {
        return registerMax;
    }
    
    
    /**
     * Return the source files.
     * 
     * @return the List containing the source files
     */
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    /**
     * The debug variable, to know which debug is set.
     */
    private int debug = 0;
    /**
     * The differents boolean to enable the right parameters.
     */
    private boolean parse = false;
    private boolean verification = false;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean noCheck = false;
    /**
     * The max number of register.
     */
    private int registerMax = 15;
    /**
     * The array of sourcefiles.
     */
    private List<File> sourceFiles = new ArrayList<File>();
    
    
    /** 
     * Parse the differents arguments, and set the options.
     * 
     * @param args the arguments of the compiler as an array of String
     * @throws CLIException if an error happens during the parsing od the arguments
     */
    public void parseArgs(String[] args) throws CLIException {
        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-b":
                    printBanner = true;
                    break;
                case "-p":
                    if (verification) {
                        throw new CLIException("-v and -p are incompatible");
                    }
                    parse = true;
                    break;
                case "-h":
                    displayUsage();
                    System.exit(0);
                    break;
                case "-v":
                    if (parse) {
                        throw new CLIException("-v and -p are incompatible");
                    }
                    verification = true;
                    break;
                case "-d":
                    debug++;
                    break;
                case "-n":
                    noCheck = true;
                    break;
                case "-P":
                    parallel = true;
                    break;
                case "-r":
                    // get the X argument
                    i++;
                    try {
                        if (i >= args.length) {
                            throw new Exception();
                        }
                        int x = Integer.parseInt(args[i]);
                        if (x < 4 || x > 16) {
                            throw new Exception();
                        }
                        this.registerMax = x - 1;
                    } catch (Exception e) {
                        throw new CLIException("Argument X must be between 4 and 16 for option -r");
                    }
                    break;
                default:
                    sourceFiles.add(new File(args[i]));
                    break;
            }
        }
        switch (getDebug()) {
            case QUIET: break; // keep default
            case INFO:
                logger.setLevel(Level.INFO); break;
            case DEBUG:
                logger.setLevel(Level.DEBUG); break;
            case TRACE:
                logger.setLevel(Level.TRACE); break;
            default:
                logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
    }

    /**
     * Print the different options.
     */
    protected void displayUsage() {
        System.out.println("Usage: decac [options] file.deca ...");
        System.out.println("Options:");
        System.out.println("  -b : print banner with the team's name");
        System.out.println("  -p : parse only and display decompiled code");
        System.out.println("  -v : verify only");
        System.out.println("  -d : activate debug, repeat for more traces");
        System.out.println("  -P : parallel compilation");
        System.out.println("  -n : continue execution even with exceptions: divide by 0, overflow, no return, forbidden cast, pointer on null, stack overflow");
        System.out.println("  -r X : limit the number of registers to R0 to R{X-1} (4 <= X <= 16)");
    }

    /**
     * Print the banner.
     */
    protected void displayBanner() {
        System.out.println("===============================================================");
        System.out.println("            Groupe 10 ---- Donkey Kong    ");
        System.out.println("===============================================================");
    }
}
