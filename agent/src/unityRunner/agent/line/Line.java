package unityRunner.agent.line;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 15/12/2011
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class Line {
    public enum Type {
        Normal,
        Warning,
        Failure,
        Error
    }

    protected String regex;
    protected Type type;

    public Line(String regex, Type type) {
        this.regex = regex;
        this.type = type;
    }

    public boolean matches(String message) {
        return message.matches(regex);
    }

    public Type getType() {
        return type;
    }
}
