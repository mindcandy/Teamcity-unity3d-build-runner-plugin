package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 14/12/2011
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class RefreshBlock extends Block
{
    public RefreshBlock()
    {
        beginning = "Refresh:.*";
        end = "Refresh:.*";

        name = "Refresh";
    }

    @Override
    public boolean matchesEnd(String message)
    {
        return message.matches(end) == false;
    }
}
