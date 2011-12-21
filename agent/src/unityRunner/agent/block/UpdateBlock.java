package unityRunner.agent.block;


public class UpdateBlock extends Block {
    public UpdateBlock() {
        beginning = "Updating (.+) - GUID: .*";
        end = "\\s*done: hash - .+";

        name = "Update";
    }

}