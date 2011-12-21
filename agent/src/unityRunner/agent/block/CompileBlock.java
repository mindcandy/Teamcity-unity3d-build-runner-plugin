package unityRunner.agent.block;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 15/12/2011
 * Time: 16:31
 * To change this template use File | Settings | File Templates.
 */
public class CompileBlock extends Block {
    public CompileBlock() {
        beginning = "-----Compiler Commandline Arguments:";
        end = "-----EndCompilerOutput---------------";

        name = "Compile";
    }

}
