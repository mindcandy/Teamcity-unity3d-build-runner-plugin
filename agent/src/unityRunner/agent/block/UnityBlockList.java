package unityRunner.agent.block;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 16/12/2011
 * Time: 09:52
 * To change this template use File | Settings | File Templates.
 */
public class UnityBlockList
{
    static public List<Block> blocks = Arrays.asList((Block) new PlayerStatisticsBlock(),
                                                     (Block) new CompileBlock());
    //                                                     (Block) new ReplaceGUIBlock(),
    //                                                 (Block) new RefreshBlock());
}
