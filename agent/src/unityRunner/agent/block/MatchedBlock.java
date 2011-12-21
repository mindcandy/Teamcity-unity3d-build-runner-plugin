package unityRunner.agent.block;

/**
 * store a matched block + some state
 */
public class MatchedBlock {
    
    public final Block block;
    public final String name;
    public final Block.MatchType matchType;
    
    public MatchedBlock(Block block, String name, Block.MatchType matchType)
    {
        this.block = block;
        this.name = name;
        this.matchType = matchType;
    }
    
    public String getName() 
    {
        return name;
    }
    
    public Block.MatchType matchesEnd(String message)
    {
        return block.matchesEnd(message);
    }
}
