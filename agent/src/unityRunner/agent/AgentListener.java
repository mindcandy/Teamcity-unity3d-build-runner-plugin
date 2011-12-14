package unityRunner.agent;

import unityRunner.common.PluginConstants;
import jetbrains.buildServer.agent.AgentLifeCycleAdapter;
import jetbrains.buildServer.agent.AgentLifeCycleListener;
import jetbrains.buildServer.agent.BuildAgent;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class AgentListener extends AgentLifeCycleAdapter {
  public AgentListener(@NotNull EventDispatcher<AgentLifeCycleListener> dispatcher) {
    dispatcher.addListener(this);
  }

  @Override
  public void agentInitialized(@NotNull final BuildAgent agent)
  {
    Loggers.AGENT.info("Plugin '" + PluginConstants.RUN_TYPE + " is running.");
  }
}
