package unityRunner.agent;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.log.Loggers;
import org.apache.commons.io.FilenameUtils;
import unityRunner.common.PluginConstants;

import java.util.ArrayList;
import java.util.List;
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
        Mac,
        Unsupported
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
    final String unityVersion;
    final String detectedUnityVersionPath;

    final Platform platform;
    final java.io.File cleanedLogPath;

    final boolean ignoreLogBefore;
    final String ignoreLogBeforeText;

    final public static String MacPlistRelativePath = "Unity.app/Contents/Info.plist";
    final public static String MacUnityExecutableRelativePath = "Unity.app/Contents/MacOS/Unity";
    final public static String WindowsUnityExecutableRelativePath = "Editor\\unity.exe";

    final static String windowsLogPath = System.getenv("LOCALAPPDATA") + "\\Unity\\Editor\\Editor.log";
    final static String macLogPath = System.getProperty("user.home") + "/Library/Logs/Unity/Editor.log";

    /**
     * construct new Unity Runner configuration
     * @param agentConfiguration current agent configuration
     * @param runnerParameters current runner parameters
     * @param agentRunningBuild the agent running the build
     */
    public UnityRunnerConfiguration(BuildAgentConfiguration agentConfiguration,
                                    Map<String, String> runnerParameters,
                                    AgentRunningBuild agentRunningBuild) {

        platform = detectPlatform(agentConfiguration);
        quit = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_QUIT);
        batchMode = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_BATCH_MODE);
        noGraphics = Parameters.getBoolean(runnerParameters, PluginConstants.PROPERTY_NO_GRAPHICS);
        projectPath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_PROJECT_PATH));

        // executable path CAN be overridden
        unityExecutablePath = FilenameUtils.separatorsToSystem(
                Parameters.getString(runnerParameters, PluginConstants.PROPERTY_UNITY_EXECUTABLE_PATH));

        unityVersion = Parameters.getString(runnerParameters, PluginConstants.PROPERTY_UNITY_VERSION);
        if (unityVersion != null) {
            // look up the path to the specified unity version in Agent Configuration Parameters
            detectedUnityVersionPath = Parameters.getString(
                    agentConfiguration.getConfigurationParameters(),
                    "unity." + unityVersion);
        } else {
            // TODO: use 'latest' version of unity that was previously found
            detectedUnityVersionPath = null;
        }

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
        //  executable path can be overridden
        if (unityExecutablePath != null && !unityExecutablePath.equals("")) {
            return unityExecutablePath;
        }

        // use the detected path for this unity version
        if (detectedUnityVersionPath != null && !detectedUnityVersionPath.equals("")) {
            return detectedUnityVersionPath;
        }

        Loggers.AGENT.error("could not find a path to Unity");
        return null;
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

    /**
     * detect the platform
     * @param agentConfiguration current configuration
     * @return Platform
     */
    static Platform detectPlatform(BuildAgentConfiguration agentConfiguration) {
        if (agentConfiguration.getSystemInfo().isWindows()) {
            return Platform.Windows;
        } else if (agentConfiguration.getSystemInfo().isMac()) {
            return Platform.Mac;
        } else {
            return Platform.Unsupported;
        }
    }

    /**
     * get path of unity log
     * @param platform platform
     * @return log path or null if not supported
     */
    static String getUnityLogPath(Platform platform) {
        switch (platform) {
            case Windows:
                return windowsLogPath;
            case Mac:
                return macLogPath;
            default:
                return null;
        }
    }


    /**
     * add location to the array only if it is non-null and non-empty
     * @param location location
     * @param locations list of locations
     */
    private static void addLocation(String location, List<String> locations) {
        if ((location != null) && !location.isEmpty()) {
            locations.add(location);
        }
    }

    /**
     * get list of possible unity locations e.g /Applications or "\Program Files"
     * @param platform current platform
     * @return list of locations - may be empty
     */
    static List<String> getPossibleUnityLocations(Platform platform) {
        List<String> locations = new ArrayList<>(2);

        switch (platform) {
            case Windows:
                // on Windows we have potentially two locations for 32 and 64 bit apps
                addLocation(System.getenv("ProgramFiles"), locations);
                addLocation(System.getenv("%programfiles% (x86)"), locations);

            case Mac:
                // on Mac there is only one location for apps.
                addLocation("/Applications", locations);
        }

        return locations;
    }

}


