package unityRunner.agent;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class Parameters
{
    final private Map<String, String> parameters;

    public Parameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }

    boolean getBooleanParameter(String name)
    {
        if(parameters.get(name) == null || parameters.get(name) != "true")
            return false;

        return true;
    }

    String getStringParameter(String name)
    {
        if(parameters.get(name) == null)
            return "";

        return parameters.get(name);
    }
}
