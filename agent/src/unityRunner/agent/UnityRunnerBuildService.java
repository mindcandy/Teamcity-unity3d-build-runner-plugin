package unityRunner.agent;

import unityRunner.common.PluginConstants;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.util.StringUtil;
import jetbrains.buildServer.log.Loggers;
import org.jetbrains.annotations.NotNull;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnityRunnerBuildService extends CommandLineBuildService {
    private final ArtifactsWatcher artifactsWatcher;

    public UnityRunnerBuildService(ArtifactsWatcher artifactsWatcher) {
        this.artifactsWatcher = artifactsWatcher;
    }

    @Override
    public void afterInitialized()
    {
        //UnityRunnerConfiguration config = createArgs();
        //UnityRunner runner = new UnityRunner(config);
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {

        UnityRunnerConfiguration config = createArgs();
        UnityRunner runner = new UnityRunner(config);

        return new SimpleProgramCommandLine(getRunnerContext(), config.getUnityPath(), runner.getArgs());
    }

    private UnityRunnerConfiguration createArgs()
    {
       Parameters parameters = new Parameters(getBuild().getRunnerParameters());
       UnityRunnerConfiguration config = new UnityRunnerConfiguration(parameters.getBooleanParameter(PluginConstants.PROPERTY_QUIT),
                                                                      parameters.getBooleanParameter(PluginConstants.PROPERTY_BATCH_MODE),
                                                                      parameters.getBooleanParameter(PluginConstants.PROPERTY_NO_GRAPHICS),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_PROJECT_PATH),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_EXECUTE_METHOD),
                                                                      UnityRunnerConfiguration.Platform.Mac);

        return config;
    }
    

}
