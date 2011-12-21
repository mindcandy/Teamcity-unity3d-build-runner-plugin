package unityRunner.agent.block;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A matchable block of text
 */
public class Block {
    protected String beginning;
    protected String end;
    protected String name;
    protected MatchType beginMatchType = MatchType.Inclusive;
    protected MatchType endMatchType = MatchType.Inclusive;
 
    protected Pattern beginPattern;
    protected Pattern endPattern;
    protected Matcher beginMatcher;
    protected Matcher endMatcher;
           
    
    public void init()
    {
        beginPattern = Pattern.compile(beginning);
        endPattern = Pattern.compile(end);
        beginMatcher = beginPattern.matcher("");
        endMatcher = endPattern.matcher("");
    }

    public enum MatchType {
        None,
        Inclusive,
        Exclusive
    }

    
    protected MatchedBlock newMatchBlock(Matcher matcher, String message)
    {
        String blockName = name;
        if (matcher.groupCount() > 0) {
            blockName = matcher.group(1);
        }
        return new MatchedBlock(this, blockName, beginMatchType);
    }

    public MatchedBlock matchesBeginning(String message) {
        beginMatcher.reset(message);
        if (beginMatcher.matches()) {
            return newMatchBlock(beginMatcher, message);
        } else {
            return null;
        }
    }

    public MatchType matchesEnd(String message) {
        endMatcher.reset(message);
        if (endMatcher.matches())
            return endMatchType;
        else
            return MatchType.None;
    }

    public String getName() {
        return name;
    }
}
