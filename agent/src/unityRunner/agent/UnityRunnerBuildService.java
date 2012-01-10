package unityRunner.agent;

import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.runner.BuildServiceAdapter;
import jetbrains.buildServer.agent.runner.ProgramCommandLine;
import jetbrains.buildServer.agent.runner.TerminationAction;
import org.jetbrains.annotations.NotNull;

public class UnityRunnerBuildService extends BuildServiceAdapter {
    private UnityRunner runner;

    public UnityRunnerBuildService() {
    }

    @Override
    public void afterInitialized() {
        runner = new UnityRunner(getConfig(), new LogParser(getLogger()));
    }

    @Override
    public void beforeProcessStarted() {
        runner.start();
    }

    @NotNull
    @Override
    public ProgramCommandLine makeProgramCommandLine() throws RunBuildException {
        return createProgramCommandline(runner.getExecutable(), runner.getArgs());
    }

    @NotNull
    private UnityRunnerConfiguration getConfig() {

        return new UnityRunnerConfiguration(getAgentConfiguration(), getRunnerParameters(), getBuild());
    }

    @Override
    @NotNull
    public TerminationAction interrupt() {
        runner.stop();
        return super.interrupt();
    }

    @Override
    public void afterProcessFinished() {
        // called when process is finished, BEFORE getting return code
        runner.stop();
    }

    @Override
    public void afterProcessSuccessfullyFinished() {
        runner.optionallyCleanupAfter();
    }
}
