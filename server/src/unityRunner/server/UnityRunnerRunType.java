package unityRunner.server;

import com.intellij.openapi.util.text.StringUtil;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import unityRunner.common.PluginConstants;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class UnityRunnerRunType extends RunType {
    public UnityRunnerRunType(final RunTypeRegistry registry) {
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return PluginConstants.RUN_TYPE;
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return PluginConstants.RUNNER_DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return PluginConstants.RUNNER_DESCRIPTION;
    }

    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor()
    {
        return new PropertiesProcessor()
        {
            public Collection<InvalidProperty> process(Map<String, String> properties)
            {
                if(noBuildTarget(properties))
                    return invalidBuildTarget();

                return Collections.emptySet();
            }

            private Set<InvalidProperty> invalidBuildTarget()
            {
                return Collections.singleton(new InvalidProperty(PluginConstants.PROPERTY_BUILD_PATH,
                                                                "Please set build path."));
            }

            private boolean noBuildTarget(Map<String, String> properties)
            {
                if(properties.containsKey(PluginConstants.PROPERTY_BUILD_PLAYER) == true &&
                   properties.get(PluginConstants.PROPERTY_BUILD_PLAYER).equals("") == false)
                {
                    if(properties.containsKey(PluginConstants.PROPERTY_BUILD_PATH) == false ||
                       properties.get(PluginConstants.PROPERTY_BUILD_PATH).equals(""))
                    {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return "editRunnerRunParameters.jsp";
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return "viewRunnerRunParameters.jsp";
    }

    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        return null;
    }
}
