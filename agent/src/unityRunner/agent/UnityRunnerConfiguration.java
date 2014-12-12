package unityRunner.agent;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import org.apache.commons.io.FilenameUtils;
import unityRunner.common.PluginConstants;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: clement.dagneau
 * Date: 13/12/2011
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */


public class UnityRunnerConfiguration {
    enum Platform {
        Windows,
        Mac
    }

    final String unityExecutablePath;
    final boolean quit;
    final boolean batchMode;
    final boolean noGraphics;
    final boolean clearBefore;
    final boolean cleanAfter;
    final boolean useCleanedLog;
    final boolean warningsAsErrors;
    final String lineListPath;
    final String projectPath;
    final String executeMethod;
    final String buildPlayer;
    final String buildPath;
    final String extraOpts;

    final Platform platform;
    final java.io.File cleanedLogPath;

    final boolean ignoreLogBefore;
    final String ignoreLogBeforeText;

    final static String windowsUnityPath = "C:\\Program Files (x86)\\Unity\\Editor\\unity.exe";
    final static String macUnityPath = "/Applications/Unity/Unity.app/Contents/MacOS/Unity";

    final static String windowsLogPath = System.getenv("LOCALAPPDATA") + "\\Unity\\Editor\\Editor.log";
    final static String macLogPath = System.getProperty("user.home") + "/Library/Logs/Unity/Editor.log";

    public UnityRunnerConfiguration(BuildAgentConfiguration agentConfiguration,
                                    Map<String, String> runnerParameters,
                                    AgentRunningBuild agentRunningBuild) {

        if (agentConfiguration.getSystemInfo().isWindows()) {
            platform = UnityRunnerConfiguration.Platform.Windows;
        } else {
            platform = UnityRunnerConfiguration.Platform.Mac;
        }
        quit = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_QUIT);
        batchMode = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_BATCH_MODE);
        noGraphics = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_NO_GRAPHICS);
        projectPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_PROJECT_PATH));
        unityExecutablePath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_UNITY_EXECUTABLE_PATH));
        lineListPath = FilenameUtils.separatorsToSystem(Parameters.getString(runnerParameters, PluginConstants.PROPERTY_LINELIST_PATH));
        executeMethod = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_EXECUTE_METHOD);
        buildPlayer = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_PLAYER);
        buildPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_PATH));
        extraOpts = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_BUILD_EXTRA);

        clearBefore = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_CLEAR_OUTPUT_BEFORE);
        cleanAfter = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_CLEAN_OUTPUT_AFTER);
        warningsAsErrors = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_WARNINGS_AS_ERRORS);

        // set cleaned log path to %temp%/cleaned-%teamcity.build.id%.log
        cleanedLogPath = new java.io.File(
                agentRunningBuild.getBuildTempDirectory(),
                String.format("cleaned-%d.log", agentRunningBuild.getBuildId()) );
        useCleanedLog = true;

        ignoreLogBefore = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_LOG_IGNORE);
        ignoreLogBeforeText = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_LOG_IGNORE_TEXT);

    }

    String getUnityPath() {
        if(unityExecutablePath != null && !unityExecutablePath.equals(""))
            return unityExecutablePath;

        return getUnityPath(platform);
    }

    String getUnityLogPath() {
        return getUnityLogPath(platform);
    }
    
    String getCleanedLogPath() {
        return cleanedLogPath.getPath();
    }
    
    String getInterestedLogPath() {
        if (useCleanedLog) {
            return getCleanedLogPath();
        } else {
            return getUnityLogPath();
        }
    }

    static String getUnityPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsUnityPath;
            case Mac:
                return macUnityPath;
        }

        return null;
    }

    static String getUnityLogPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsLogPath;
            case Mac:
                return macLogPath;
        }

        return null;
    }
}


