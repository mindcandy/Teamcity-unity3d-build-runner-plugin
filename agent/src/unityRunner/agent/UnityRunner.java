package unityRunner.agent;

import org.apache.commons.io.input.Tailer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class UnityRunner implements Runnable
{
    final UnityRunnerConfiguration configuration;
    private boolean stop = false;
    private final LogParser logParser;

    UnityRunner(UnityRunnerConfiguration configuration, LogParser logParser)
    {
        this.configuration = configuration;
        this.logParser = logParser;
    }

    List<String> getArgs()
    {
        List<String> args = new ArrayList<String>();

        if(configuration.batchMode)
            args.add("-batchmode");

        if(configuration.noGraphics)
            args.add("-nographics");

        if(configuration.projectPath != "")
        {
            args.add("-projectPath");
            args.add(configuration.projectPath);
        }

        if(configuration.executeMethod != "")
        {
            args.add("-executeMethod");
            args.add(configuration.executeMethod);
        }


        if(configuration.buildPlayer != "")
        {
            args.add(String.format("-%s", configuration.buildPlayer));
            args.add(String.format("%s", configuration.buildPath));
        }

        if(configuration.quit)
            args.add("-quit");

        return args;
    }

    public void run()
    {
        initialise();

        File file = new File(configuration.getUnityLogPath());
        TailerListener listener = new TailerListener(this);
        Tailer tailer = Tailer.create(file, listener);

        while(stop == false)
        {
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


