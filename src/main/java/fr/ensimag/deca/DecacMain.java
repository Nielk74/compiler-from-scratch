package fr.ensimag.deca;

import static org.mockito.Answers.valueOf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl10
 * @date 25/04/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    private static boolean compileParallel(CompilerOptions options) {
        boolean error = false;
        List<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
        for (File file : options.getSourceFiles()) {
            Callable<Boolean> c = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    DecacCompiler compiler = new DecacCompiler(options, file);
                    if (compiler.compile()) {
                        return Boolean.FALSE;
                    }
                    return Boolean.TRUE;
                }

            };
            tasks.add(c);
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        try {
            // long start = System.currentTimeMillis();
            List<Future<Boolean>> results = exec.invokeAll(tasks);
            for (Future<Boolean> res : results) {
                Boolean success = res.get();
                if (!success) {
                    error = true;
                }
            }
            // long end = System.currentTimeMillis();
            // LOG.info("Compilation finished in " + (end - start) + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            exec.shutdown();
        }
        return error;
    }

    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            options.displayBanner();
        }
        if (options.getSourceFiles().isEmpty()) {
            options.displayUsage();
        }
        if (options.getParallel()) {
            error = compileParallel(options);
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
