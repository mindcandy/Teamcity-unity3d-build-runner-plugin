package unityRunner.agent;

import jetbrains.buildServer.messages.BuildMessage1;
import jetbrains.buildServer.messages.Status;
import unityRunner.agent.block.Block;
import unityRunner.agent.block.MatchedBlock;
import unityRunner.agent.block.UnityBlockList;
import unityRunner.agent.line.Line;
import unityRunner.agent.line.UnityLineList;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 15/12/2011
 * Time: 16:02
 */


public class LogParser {
    private final Stack<MatchedBlock> blockStack = new Stack<MatchedBlock>();
    private final jetbrains.buildServer.agent.BuildProgressLogger logger;

    LogParser(jetbrains.buildServer.agent.BuildProgressLogger logger) {
        this.logger = logger;
        
        for (Block block : UnityBlockList.blocks)
            block.init();
    }


    public void logActivityStart(String name)
    {
        logger.activityStarted(name, "DefaultMessage");
    }

    public void logActivityEnd(String name)
    {
        logger.activityFinished(name, "DefaultMessage");
    }



    private void logBlockStart(MatchedBlock block) {
        logActivityStart(block.getName());
        blockStack.push(block);
    }

    private void logBlockEnd() {
        logActivityEnd(blockStack.pop().getName());
    }


    public void log(String message) {
        // Check if new message is the end of the current block (if it exists).
        if (!blockStack.empty()) {
            Block.MatchType match = blockStack.peek().matchesEnd(message);

            if (match == Block.MatchType.Inclusive) {
                // include this line in the block
                logLine(message);
                logBlockEnd();
                return;

            } else if (match == Block.MatchType.Exclusive) {
                logBlockEnd();
            }
        }

        // Check if line is the beginning of a new block.
        for (Block block : UnityBlockList.blocks) {

            MatchedBlock matchedBlock = block.matchesBeginning(message);
            if (null != matchedBlock) {

                if (matchedBlock.matchType == Block.MatchType.Inclusive) {

                    logBlockStart(matchedBlock);
                    break;

                } else if (matchedBlock.matchType == Block.MatchType.Exclusive) {

                    // exclude the line from the block, so log it out now
                    logLine(message);
                    logBlockStart(matchedBlock);
                    return;
                }
            }
        }

        // no blocks starting/ending so just log out!
        logLine(message);
    }


    private void logLine(String message) {
        // Now check message
        for (Line line : UnityLineList.lines) {
            if (line.matches(message)) {
                log(message, line.getType());
                return;
            }
        }

        // There is not match. Just log a regular message.
        log(message, Line.Type.Normal);
    }

    private void log(String message, Line.Type type) {
        Status status;

        switch (type) {
            case Warning:
                status = Status.WARNING;
                break;
            case Error:
                status = Status.ERROR;
                break;
            case Failure:
                status = Status.FAILURE;
                break;
            default:
                status = Status.NORMAL;
                break;
        }

        logger.logMessage(new BuildMessage1("DefaultMessage", "Text", status, getTimestamp(), message));
    }

    public void logException(Exception e) {
        final Writer stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));

        log("Exception: " + stackTrace.toString(), Line.Type.Failure);
    }

    private Timestamp getTimestamp() {
        java.util.Date date = new java.util.Date();
        return new Timestamp(date.getTime());
    }
}
