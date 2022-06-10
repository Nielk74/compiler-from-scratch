package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl10
 * @date 25/04/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParse() {
        return parse;
    }

    public boolean getVerification() {
        return verification;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public int getRegisterMax() {
        return registerMax;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    private int debug = 0;
    private boolean parse = false;
    private boolean verification = false;
    private boolean parallel = false;
    private boolean printBanner = false;
    private int registerMax = 15;
    private List<File> sourceFiles = new ArrayList<File>();
    
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
                        this.registerMax = x;
                    } catch (Exception e) {
                        System.out.println("Argument X must be between 4 and 16 for option -r");
                        displayUsage();
                        System.exit(0);
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

    protected void displayUsage() {
        System.out.println("Usage: decac [options] file.deca ...");
        System.out.println("Options:");
        System.out.println("  -b : print banner with the team's name");
        System.out.println("  -p : parse only and display decompiled code");
        System.out.println("  -v : verify only");
        System.out.println("  -d : activate debug, repeat for more traces");
        System.out.println("  -P : parallel compilation");
        System.out.println("  -r X : limit the number of registers to R0 to R{X-1} (4 <= X <= 16)");
    }

    protected void displayBanner() {
        System.out.println("===============================================================");
        System.out.println("            Groupe 10 ---- Donkey Kong    ");
        System.out.println("===============================================================");
    }
}
