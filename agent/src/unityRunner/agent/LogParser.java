package unityRunner.agent;

import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.Status;
import unityRunner.agent.block.Block;
import unityRunner.agent.block.UnityBlockList;
import unityRunner.agent.line.Line;
import unityRunner.agent.line.UnityLineList;

import java.sql.Timestamp;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 15/12/2011
 * Time: 16:02
 */


public class LogParser
{
    private final Stack<Block> blockStack = new Stack<Block>();
    private final jetbrains.buildServer.agent.BuildProgressLogger logger;

    LogParser(jetbrains.buildServer.agent.BuildProgressLogger logger)
    {
        this.logger = logger;
    }

    public void log(String message)
    {
        // Check if new message is the end of the current block (if it exists).
        if(!blockStack.empty())
        {
            if(blockStack.peek().matchesEnd(message))
            {
                logger.activityFinished(blockStack.pop().getName(), "DefaultMessage");
            }
        }

        // Check if line is the beginning of a new block.
        for (Block block : UnityBlockList.blocks)
        {
            if(block.matchesBeginning(message))
            {
                blockStack.push(block);
                logger.activityStarted(block.getName(), "DefaultMessage");
                break;
            }
        }

        // Now check message
        boolean  match = false;
        for (Line line : UnityLineList.lines)
        {
            if(line.matches(message))
            {
                log(message, line.getType());
                match = true;
                break;
            }
        }

        // There is not match. Just log a regular message.
        if(!match)
        {
            log(message, Line.Type.Normal);
        }
            
    }
    
    private void log(String message, Line.Type type)
    {
        Status status;
        
        switch (type)
        {
            case Warning : status = Status.WARNING; break;
            case Error : status = Status.ERROR; break;
            case Failure : status = Status.FAILURE; break;
            default : status = Status.NORMAL; break;
        }
        
        logger.logMessage(new BuildMessage1("DefaultMessage", "Text", status, getTimestamp(), message));
    }

    private Timestamp getTimestamp()
    {
        java.util.Date date= new java.util.Date();
        return new Timestamp(date.getTime());
    }
}
