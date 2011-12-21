package unityRunner.agent;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.input.Tailer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:36
 */
public class UnityRunner implements Runnable
{
    final UnityRunnerConfiguration configuration;
    private volatile boolean stop = false;
    private final LogParser logParser;

    UnityRunner(UnityRunnerConfiguration configuration, LogParser logParser)
    {
        this.configuration = configuration;
        this.logParser = logParser;
    }

    /**
     * format a path to have system separators
     * @param path the path to prepare
    *  @return the prepared path
     */
    private static String preparePath(String path)
    {
        return FilenameUtils.separatorsToSystem(path);
    }

    /**
     * @return  executable name/path
     */
    @NotNull
    String getExecutable() {
        return configuration.getUnityPath();
    }

    /**
     *
     * @return get arguments for executable
     */
    @NotNull
    List<String> getArgs()
    {
        List<String> args = new ArrayList<String>();

        if(configuration.batchMode)
            args.add("-batchmode");

        if(configuration.noGraphics)
            args.add("-nographics");

        if(configuration.quit)
            args.add("-quit");

        if(!configuration.buildPlayer.equals(""))
        {
            args.add(String.format("-%s", configuration.buildPlayer));
            args.add(String.format("%s", preparePath(configuration.buildPath)));
        }

        if(!configuration.projectPath.equals(""))
        {
            args.add("-projectPath");
            args.add(preparePath(configuration.projectPath));
        }

        if(!configuration.executeMethod.equals(""))
        {
            args.add("-executeMethod");
            args.add(configuration.executeMethod);
        }

        return args;
    }

    public void run()
    {
        initialise();

        File file = new File(configuration.getUnityLogPath());
        TailerListener listener = new TailerListener(this);
        Tailer tailer = Tailer.create(file, listener);

        while(!stop)
        {
            // sleep so we don't busy-wait
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        tailer.stop();
    }

    public void stop()
    {
        stop = true;
    }

    private void initialise()
    {
        deleteLogFile(configuration.getUnityLogPath());
    }
    
    private void deleteLogFile(String path)
    {
        File logFile = new File(path);

        if(logFile.exists())
        {
            logFile.delete();
        }
    }

    void logMessage(String message)
    {
        logParser.log(message);
    }
}


