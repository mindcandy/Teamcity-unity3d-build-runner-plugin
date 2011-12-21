package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 14/12/2011
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
public class PlayerStatisticsBlock extends Block {
    public PlayerStatisticsBlock() {
        beginning = "\\*\\*\\*Player size statistics\\*\\*\\*";
        end = "Unloading.*";

        name = "Player statistics";
    }
}
