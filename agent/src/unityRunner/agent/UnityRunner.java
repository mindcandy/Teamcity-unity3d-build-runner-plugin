package unityRunner.agent;

import java.util.ArrayList;
import java.util.List;
//import java.apache.common.io.input.Trailer

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

    UnityRunner(UnityRunnerConfiguration configuration)
    {
        this.configuration = configuration;
    }

    List<String> getArgs()
    {
        List<String> args = new ArrayList<String>();

        if(configuration.batchMode)
            args.add("-batchmode");

        if(configuration.noGraphics)
            args.add("-nographics");

        if(configuration.projectPath != "")
            args.add(String.format("-projectPath %s", configuration.projectPath));

        if(configuration.executeMethod != "")
            args.add(String.format("-executeMethod %s", configuration.executeMethod));

        if(configuration.quit)
            args.add("-quit");

        return args;
    }

    public void run()
    {

    }
}
