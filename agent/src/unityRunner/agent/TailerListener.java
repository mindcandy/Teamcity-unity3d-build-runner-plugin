package unityRunner.agent;

import org.apache.commons.io.input.TailerListenerAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 14/12/2011
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */

public class TailerListener extends TailerListenerAdapter
{
    private final UnityRunner runner;
    public TailerListener(UnityRunner runner)
    {
        this.runner = runner;
    }

    public void handle(String line)
    {
        runner.logMessage(line);
    }
}
