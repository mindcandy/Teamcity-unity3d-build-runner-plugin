package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: markb
 * Date: 20/12/2011
 * Time: 16:32
 */
public class LightmapBlock extends Block
{
    public LightmapBlock()
    {
        name = "Lightmap";
        beginning = "---- Lightmapping Start for .* ----";
        end = "---- Lightmapping End for .* ----";
    }
}
