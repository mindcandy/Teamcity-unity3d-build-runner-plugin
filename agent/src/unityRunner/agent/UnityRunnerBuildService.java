package unityRunner.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.artifacts.ArtifactsWatcher;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.SimpleProgramCommandLine;
import jetbrains.buildServer.agent.runner.TerminationAction;
import org.jetbrains.annotations.NotNull;
import unityRunner.common.PluginConstants;

public class UnityRunnerBuildService extends CommandLineBuildService
{

    private final ArtifactsWatcher artifactsWatcher;
    private UnityRunnerConfiguration config;
    private UnityRunner runner;
    private Thread runnerThread;

    public UnityRunnerBuildService(ArtifactsWatcher artifactsWatcher)
    {
        this.artifactsWatcher = artifactsWatcher;
    }

    @Override
    public void afterInitialized()
    {
        config = createConfig();
        runner = new UnityRunner(config, getLogger());

        runnerThread = new Thread(runner);
        runnerThread.start();
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException
    {
        return new SimpleProgramCommandLine(getRunnerContext(), config.getUnityPath(), runner.getArgs());
    }

    private UnityRunnerConfiguration createConfig()
    {
       Parameters parameters = new Parameters(getBuild().getRunnerParameters());
       UnityRunnerConfiguration.Platform platform = UnityRunnerConfiguration.Platform.Mac;

        if(getBuild().getAgentConfiguration().getSystemInfo().isWindows())
            platform = UnityRunnerConfiguration.Platform.Windows;

       UnityRunnerConfiguration config = new UnityRunnerConfiguration(parameters.getBooleanParameter(PluginConstants.PROPERTY_QUIT),
                                                                      parameters.getBooleanParameter(PluginConstants.PROPERTY_BATCH_MODE),
                                                                      parameters.getBooleanParameter(PluginConstants.PROPERTY_NO_GRAPHICS),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_PROJECT_PATH),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_EXECUTE_METHOD),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_BUILD_PLAYER),
                                                                      parameters.getStringParameter(PluginConstants.PROPERTY_BUILD_PATH),
                                                                      platform);

        return config;
    }

    @Override
    public TerminationAction interrupt()
    {
        runner.stop();
        return super.interrupt();
    }
}
