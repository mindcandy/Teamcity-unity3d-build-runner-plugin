package unityRunner.agent.line;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 15/12/2011
 * Time: 16:51
 */
public class UnityLineList
{
    public static List<Line> lines = Arrays.asList(

            // Warnings
            new Line("Script attached to.*?is missing or no valid script is attached.", Line.Type.Warning),
            new Line(".*?warning CS\\d+.*?", Line.Type.Warning),
            new Line("There are inconsistent line endings in the.*?", Line.Type.Warning),
            new Line("This might lead to incorrect line numbers in stacktraces and compiler errors.*?", Line.Type.Warning),
            new Line("WARNING.*", Line.Type.Warning),

            // Errors
            new Line(".*?error CS\\d+.*?", Line.Type.Error),
            new Line("Compilation failed:.*", Line.Type.Error),
            new Line("Scripts have compiler errors\\..*", Line.Type.Error));
}