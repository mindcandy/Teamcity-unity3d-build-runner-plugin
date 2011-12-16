package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 14/12/2011
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */

public class Block
{
    protected String beginning;
    protected String end;
    protected String name;

    public boolean matchesBeginning(String message)
    {
        return message.matches(beginning);
    }
    
    public boolean matchesEnd(String message)
    {
        return message.matches(end);
    }
    
    public String getName()
    {
        return name;
    }
}
