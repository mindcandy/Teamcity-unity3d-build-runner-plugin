package unityRunner.agent;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */


public class UnityRunnerConfiguration
{
    enum Platform
    {
        Windows,
        Mac
    }

    final boolean quit;
    final boolean batchMode;
    final boolean noGraphics;
    final String projectPath;
    final String executeMethod;

    final Platform platform;
    
    final String windowsUnityPath = "";
    final String macUnityPath = "/Applications/Unity/Unity.app/Contents/MacOS/Unity";
    
    final String windowsLogPath = "";
    final String macLogPath = System.getProperty("user.home") + "/Library/Logs/Unity/Editor.log";

    public UnityRunnerConfiguration(boolean quit,
                                    boolean batchMode,
                                    boolean noGraphics,
                                    String projectPath,
                                    String executeMethod,
                                    Platform platform)
    {
        this.quit = quit;
        this.batchMode = batchMode;
        this.noGraphics = noGraphics;
        this.projectPath = projectPath;
        this.executeMethod = executeMethod;
        this.platform = platform;
    }

    String getUnityPath()
    {
        switch (platform)
        {
            case Windows : return windowsUnityPath;
            case Mac : return macUnityPath;
        }
        
        return null;
    }
}


