/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package unityRunner.agent;


import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import jetbrains.buildServer.log.Loggers;
import org.apache.commons.configuration.plist.XMLPropertyListConfiguration;
import org.jetbrains.annotations.NotNull;
import unityRunner.common.PluginConstants;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory that builds Unity Runners and detects what versions of Unity are installed locally
 */
public class UnityRunnerBuildServiceFactory implements CommandLineBuildServiceFactory {

    public UnityRunnerBuildServiceFactory() {
    }

    @NotNull
    public CommandLineBuildService createService() {
        return new UnityRunnerBuildService();
    }

    /**
     * construct and return AgentBuildRunnerInfo which will be used to determine which builds can run
     * @return an AgentBuildRunnerInfo
     */
    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {

            /**
             * @return name of the runner, should match RunType.getType on the server side
             */
            @NotNull
            public String getType() {
                return PluginConstants.RUN_TYPE;
            }

            /**
             * check if the build runner is compatible with the machine e.g. Is any version of Unity installed?
             *
             * Note that the build runner can also add Configuration Parameters to the build configuration.
             * This plugin detects installed Unity versions and adds them as Agent Configuration Parameters.
             * Then, Agent Requirements can be used to select only the Agents with the required version of Unity.
             *
             * @param agentConfiguration - current build agent configuration
             * @return true if the runner can run builds in the environment of build agent.
             *  For example, returning 'false' is a way for a runner to refuse to run on a system that is not supported by the runner
             *  If 'false' is returned, the Agent won't register the runner and it won't be advertised to the build server server during agent's registration
             */
            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                return setupConfigurationParameters(agentConfiguration);
            }

            /**
             * Setup agent configuration parameters.
             * @param agentConfiguration build agent configuration.
             * @return true if any version of Unity is detected, false otherwise
             */
            private boolean setupConfigurationParameters(@NotNull BuildAgentConfiguration agentConfiguration) {

                // find ALL the versions of unity installed
                UnityRunnerConfiguration.Platform platform = UnityRunnerConfiguration.detectPlatform(agentConfiguration);
                if (platform == UnityRunnerConfiguration.Platform.Unsupported) {
                    // not a supported platform
                    return false;
                }

                Map<String,String> foundUnityVersions = new HashMap<String,String>();

                for(String location : UnityRunnerConfiguration.getPossibleUnityLocations(platform)) {
                    //  search for <location>/Unity * folders
                    Loggers.AGENT.info("Search for Unity in " + location);
                    findUnityVersionsIn(location, platform, foundUnityVersions);
                }

                // find and record the *latest* version of Unity in unity.latest
                String latestVersion = "0";
                String latestVersionPath = null;

                // add those unity versions to the Agent Configuration
                for (Map.Entry<String,String> foundVersion : foundUnityVersions.entrySet()) {

                    final String version = foundVersion.getKey();
                    final String path = foundVersion.getValue();

                    // add by version number -> path to Unity executable
                    // e.g. unity.4.1.2.f1 = /Applications/Unity 4.1.2/Editor/Unity
                    agentConfiguration.addConfigurationParameter(
                            PluginConstants.CONFIGPARAM_UNITY_BASE_VERSION + version,
                            path);

                    if (UnityVersionComparison.isGreaterThan(version, latestVersion)) {
                        latestVersion = version;
                        latestVersionPath = path;
                    }
                }

                if (latestVersionPath != null) {
                    // record the latest version found
                    agentConfiguration.addConfigurationParameter(
                            PluginConstants.CONFIGPARAM_UNITY_LATEST_VERSION,
                            latestVersionPath);
                    Loggers.AGENT.info("Latest unity version = " + latestVersion + " at: " + latestVersionPath);

                    // add log location (which is oddly the same regardless of version)
                    String logPath = UnityRunnerConfiguration.getUnityLogPath(platform);
                    if (logPath != null) {
                        agentConfiguration.addConfigurationParameter(
                            PluginConstants.CONFIGPARAM_UNITY_LOG_PATH, logPath);
                    }
                }

                return !foundUnityVersions.isEmpty();
            }

            /**
             * detect if unity is installed in this location
             * @param location location to test
             * @param platform current platform
             * @param foundUnityVersions map to add discovered unity versions to
             */
            private void findUnityVersionsIn(
                    String location,
                    UnityRunnerConfiguration.Platform platform,
                    Map<String,String> foundUnityVersions) {

                // NOTE: java nio 2 requires java 7 and above
                Path path = FileSystems.getDefault().getPath(location);
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, "Unity*")) {
                    for (Path entry: stream) {
                        if (platform ==  UnityRunnerConfiguration.Platform.Mac) {
                            findMacUnityVersion(entry, foundUnityVersions);
                        } else if (platform == UnityRunnerConfiguration.Platform.Windows) {
                            findWindowsUnityVersion(entry, foundUnityVersions);
                        }
                    }
                } catch (IOException e) {
                    // IOException can never be thrown by the iteration.
                    // In this snippet, it can // only be thrown by newDirectoryStream.
                    Loggers.AGENT.error("Exception getting finding unity versions :" + e.getMessage());
                }
            }

            /**
             * detect if there is a Mac Unity Editor installed in the given location
             * @param possibleUnityLocation location for Unity to be installed
             * @param foundUnityVersions map to add the (version,path) data to if found
             */
            private void findMacUnityVersion(Path possibleUnityLocation, Map<String,String> foundUnityVersions) {
                try {
                    Path unityExecutable = possibleUnityLocation.resolve(UnityRunnerConfiguration.MacUnityExecutableRelativePath);
                    Path configFilePath = possibleUnityLocation.resolve(UnityRunnerConfiguration.MacPlistRelativePath);

                    XMLPropertyListConfiguration config = new XMLPropertyListConfiguration(configFilePath.toFile());
                    String version = config.getString("CFBundleVersion");

                    if (version != null && unityExecutable.toFile().exists()) {
                        Loggers.AGENT.info("Found unity version = " + version + " at: " + unityExecutable.toString());
                        foundUnityVersions.put(version, unityExecutable.toString());
                    }

                } catch (Exception e) {
                    // had trouble detecting version
                    Loggers.AGENT.error("Exception getting unity version :" + e.getMessage());
                }
            }

            private void findWindowsUnityVersion(Path possibleUnityLocation, Map<String,String> foundUnityVersions){

                try{
                    Path unityExecutable = possibleUnityLocation.resolve(UnityRunnerConfiguration.WindowsUnityExecutableRelativePath);

                    if (unityExecutable.toFile().exists()) {
                        // found Unity.exe - read version
                        String version = FileVersionInfo.getShortVersionNumber(unityExecutable.toString());

                        // TODO: test this - if it doesn't work then look at path, or long version number?
                        if (version != null) {
                            Loggers.AGENT.info("Found unity version = " + version + " at: " + unityExecutable.toString());
                            foundUnityVersions.put(version, unityExecutable.toString());
                        }
                    }

                } catch(Exception e){
                    Loggers.AGENT.error("Exception getting unity version :" + e.getMessage());
                }

            }
        };
    }
}





