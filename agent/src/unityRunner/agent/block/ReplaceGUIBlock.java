package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 14/12/2011
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class ReplaceGUIBlock extends Block
{
    public ReplaceGUIBlock()
    {
        beginning = "\\*\\*\\* \\w+ replaces \\w+ at path .*";
        end = "\\*\\*\\* \\w+ replaces \\w+ at path .*";

        name = "Replace GUI";
    }

    @Override
    public boolean matchesEnd(String message)
    {
        return !message.matches(end);
    }
}
