package unityRunner.agent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.input.Tailer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:36
 */
public class UnityRunner {
    final UnityRunnerConfiguration configuration;
    private volatile boolean stop = false;
    private final LogParser logParser;

    UnityRunner(UnityRunnerConfiguration configuration, LogParser logParser) {
        this.configuration = configuration;
        this.logParser = logParser;
    }


    /**
     * @return executable name/path
     */
    @NotNull
    String getExecutable() {
        return configuration.getUnityPath();
    }

    /**
     * @return get arguments for executable
     */
    @NotNull
    List<String> getArgs() {
        List<String> args = new ArrayList<String>();

        if (configuration.batchMode)
            args.add("-batchmode");

        if (configuration.noGraphics)
            args.add("-nographics");

        if (configuration.quit)
            args.add("-quit");

        if (!configuration.buildPlayer.equals("")) {
            args.add(String.format("-%s", configuration.buildPlayer));
            args.add(String.format("%s", configuration.buildPath));
        }

        if (!configuration.projectPath.equals("")) {
            args.add("-projectPath");
            args.add(configuration.projectPath);
        }

        if (!configuration.executeMethod.equals("")) {
            args.add("-executeMethod");
            args.add(configuration.executeMethod);
        }

        if (configuration.useCleanedLog) {
            args.add("-cleanedLogFile");
            args.add(configuration.getCleanedLogPath());
        }

        return args;
    }


    /**
     * start the unity runner
     */
    public void start() {

        logMessage("[Starting UnityRunner]");

        Thread runnerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tailLogFile();
            }
        });
        runnerThread.start();

        if (configuration.clearBefore) {
            clearBefore();
        }
    }

    /**
     * tail the log file during running
     */
    private void tailLogFile() {
        initialise();
        logMessage("[tailing log file: " + configuration.getInterestedLogPath() + "]");

        File file = new File(configuration.getInterestedLogPath());
        TailerListener listener = new TailerListener(this);
        Tailer tailer = Tailer.create(file, listener);

        while (!stop) {
            // sleep so we don't busy-wait
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tailer.stop();
        logMessage("[log tail process end]");

    }

    /**
     * stop the runner
     */
    public void stop() {
        stop = true;
        logMessage("[Stop UnityRunner]");

    }


    /**
     * cleanup after runner
     */
    public void optionallyCleanupAfter() {
        if (configuration.cleanAfter) {
            cleanAfter();
        }
    }


    private void initialise() {
        deleteLogFile(configuration.getInterestedLogPath());
    }

    private void deleteLogFile(String path) {

        File logFile = new File(path);

        if (logFile.exists()) {
            logMessage("[delete old log file]");

            if (!logFile.delete()) {
                logMessage("[FAILED TO DELETE OLD LOG FILE]");
            }
        }
    }

    void logMessage(String message) {
        logParser.log(message);
    }

    /**
     * clear the output directory before running
     */
    private void clearBefore() {
        File outputDir = new File(configuration.buildPath);

        try {
            if (outputDir.exists()) {
                logMessage("Removing output directory: " + outputDir.getPath());
                FileUtils.deleteDirectory(outputDir);
            }

            logMessage("Creating output directory: " + outputDir.getPath());
            FileUtils.forceMkdir(outputDir);

        } catch (IOException e) {
            logParser.logException(e);
        }

    }

    /**
     * remove .svn and .meta files from the output directory after running
     */
    private void cleanAfter() {
        new OutputDirectoryCleaner(logParser).clean(new File(configuration.buildPath));
    }

}


