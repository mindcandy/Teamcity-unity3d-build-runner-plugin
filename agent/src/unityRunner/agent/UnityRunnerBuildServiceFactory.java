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
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;
import unityRunner.common.PluginConstants;

import java.io.File;

public class UnityRunnerBuildServiceFactory implements CommandLineBuildServiceFactory {
    private final ArtifactsWatcher artifactsWatcher;

    public UnityRunnerBuildServiceFactory(ArtifactsWatcher artifactsWatcher) {
        this.artifactsWatcher = artifactsWatcher;
    }

    @NotNull
    public CommandLineBuildService createService() {
        return new UnityRunnerBuildService(artifactsWatcher);
    }

    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {

            @NotNull
            public String getType() {
                return PluginConstants.RUN_TYPE;
            }

            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration)
            {
                setupConfigurationParameters(agentConfiguration);
                return agentConfiguration.getSystemInfo().isWindows() || agentConfiguration.getSystemInfo().isMac();
            }

            /**
             * Setup agent configuration parameters.
             * @param agentConfiguration build agent configuration.
             */
            public void setupConfigurationParameters(@NotNull BuildAgentConfiguration agentConfiguration)
            {
                String unityPath = null;
                String unityLog = null;

                // Get unity path and editor log path for supported platforms.
                if(agentConfiguration.getSystemInfo().isWindows())
                {
                    unityPath = UnityRunnerConfiguration.getUnityPath(UnityRunnerConfiguration.Platform.Windows);
                    unityLog  = UnityRunnerConfiguration.getUnityLogPath(UnityRunnerConfiguration.Platform.Windows);
                }
                else if(agentConfiguration.getSystemInfo().isMac())
                {
                    unityPath = UnityRunnerConfiguration.getUnityPath(UnityRunnerConfiguration.Platform.Mac);
                    unityLog  = UnityRunnerConfiguration.getUnityLogPath(UnityRunnerConfiguration.Platform.Mac);
                }

                if(unityPath != null && unityLog != null)
                {
                    File file = new File(unityPath);

                    if(file.exists())
                    {
                        agentConfiguration.addConfigurationParameter("unity.exe", unityPath);
                        // We need to find a way to automatically get Unity version number.c
                        agentConfiguration.addConfigurationParameter("unity.version", "3.4.2");
                        agentConfiguration.addConfigurationParameter("unity.log", unityLog);
                    }
                }
            }
        };
    }
}
